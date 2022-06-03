package com.wxl.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.constant.WareConstant;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.ware.service.WareSkuService;
import com.wxl.mall.ware.vo.MergeVO;
import com.wxl.mall.ware.dao.PurchaseDao;
import com.wxl.mall.ware.entity.PurchaseDetailEntity;
import com.wxl.mall.ware.entity.PurchaseEntity;
import com.wxl.mall.ware.service.PurchaseDetailService;
import com.wxl.mall.ware.service.PurchaseService;
import com.wxl.mall.ware.vo.PurchaseDoneVO;
import com.wxl.mall.ware.vo.PurchaseItemDoneVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;
    @Resource
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }


    /**
     * 查询未领取的采购单
     *
     * @param params params
     * @return pageData
     */
    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params),
                // todo 状态优化为enum
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1));

        return new PageUtils(page);
    }


    /**
     * 合并采购需求
     *
     * @param mergeVO 整单id&合并项集合
     */
    @Transactional
    @Override
    public void mergePurchase(MergeVO mergeVO) {
        // purchaseId有的话, 合并至此; 没有的话就新建
        Long purchaseId = mergeVO.getPurchaseId();
        List<Long> items = mergeVO.getItems();

        if (null == purchaseId) {
            // 新建采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());

            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();

        }

        // todo 采购单状态校验(必须是0或者1)才可以合并

        // 合并采购单(就是修改实体状态)
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> detailEntityList = items.stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();

            detailEntity.setId(item);
            detailEntity.setPurchaseId(finalPurchaseId);
            // 采购需求已经分配给采购单了, 改为已分配
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());

            return detailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(detailEntityList);

        // 合并后, 更新采购单更新时间字段
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    /**
     * 领取采购单
     *
     * @param ids 采购单ids
     */
    @Transactional
    @Override
    public void received(List<Long> ids) {
        // 1、确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> purchaseEntities = ids.stream().map(this::getById)
                // todo 这里应该是校验的, 现在先进行过滤处理吧
                .filter(purchaseEntity -> purchaseEntity.getStatus().equals(WareConstant.PurchaseStatusEnum.CREATED.getCode()) || purchaseEntity.getStatus().equals(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()))
                // 要改成的状态
                .peek(item -> {
                    item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    item.setUpdateTime(new Date());
                }).collect(Collectors.toList());

        // 2、改变采购单状态
        this.updateBatchById(purchaseEntities);

        // 3、改变采购单采购项的状态
        purchaseEntities.forEach(item -> {
            // 按照采购单的id找到下面所有的采购项
            List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> purchaseDetailUpdateList = purchaseDetailEntityList.stream().map(entity -> {
                PurchaseDetailEntity detailUpdate = new PurchaseDetailEntity();
                detailUpdate.setId(entity.getId());
                // 采购中...
                detailUpdate.setStatus(WareConstant.PurchaseDetailStatusEnum.PURCHASING.getCode());
                return detailUpdate;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(purchaseDetailUpdateList);
        });
    }


    /**
     * 完成采购单
     *
     * @param purchaseDoneVO 采购单ids
     */
    @Transactional
    @Override
    public void finishPurchase(PurchaseDoneVO purchaseDoneVO) {
        // 1、改变采购单状态, 后置操作, 需要根据采购项的状态

        // 2、改变采购项状态
        List<PurchaseDetailEntity> updateItems = new ArrayList<>();
        boolean flag = true;
        List<PurchaseItemDoneVO> items = purchaseDoneVO.getItems();
        for (PurchaseItemDoneVO item : items) {
            if (item.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.PURCHASE_FAIL.getCode())) {
                flag = false;
            } else {
                // 3、成功采购的采购单入库(库存表)
                PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(purchaseDetail.getSkuId(), purchaseDetail.getWareId(), purchaseDetail.getSkuNum());
            }

            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setStatus(item.getStatus());
            purchaseDetailEntity.setId(item.getItemId());

            updateItems.add(purchaseDetailEntity);
        }
        // 批量更新
        purchaseDetailService.updateBatchById(updateItems);

        // 改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseDoneVO.getId());
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode()
                : WareConstant.PurchaseStatusEnum.HAS_ERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());

        this.updateById(purchaseEntity);
    }

}
