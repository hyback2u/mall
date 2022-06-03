package com.wxl.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.constant.WareConstant;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.ware.controller.vo.MergeVO;
import com.wxl.mall.ware.dao.PurchaseDao;
import com.wxl.mall.ware.entity.PurchaseDetailEntity;
import com.wxl.mall.ware.entity.PurchaseEntity;
import com.wxl.mall.ware.service.PurchaseDetailService;
import com.wxl.mall.ware.service.PurchaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

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
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                // todo 状态优化为enum
                new QueryWrapper<PurchaseEntity>().eq("status", 0)
                        .or().eq("status", 1)
        );

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
    }

}
