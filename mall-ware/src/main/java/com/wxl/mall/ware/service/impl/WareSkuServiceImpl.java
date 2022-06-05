package com.wxl.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.to.SkuHasStockVO;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.common.utils.R;
import com.wxl.mall.ware.dao.WareSkuDao;
import com.wxl.mall.ware.entity.WareSkuEntity;
import com.wxl.mall.ware.feign.ProductFeignService;
import com.wxl.mall.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private WareSkuDao wareSkuDao;
    @Resource
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();

        // 添加定制查询条件 skuId,wareId
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (StringUtils.isNotBlank(skuId) && !"0".equalsIgnoreCase(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }
        if (StringUtils.isNotBlank(wareId) && !"0".equalsIgnoreCase(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }


    /**
     * 成功采购的采购单入库
     *
     * @param skuId  skuId
     * @param wareId wareId
     * @param skuNum skuNum
     */
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        // 判断有没有库存记录, 有的话更新, 没有的话新增
        List<WareSkuEntity> entityList = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (null == entityList || entityList.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStockLocked(0);

            // todo 远程调用失败处理策略, 目前先try-catch掉异常, 还有什么办法, 让异常出现以后不回滚？
            try {
                R info = productFeignService.info(skuId);
                if (info.getCode() == 0) {
                    Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {
                log.info("e: {}", e.getMessage());
            }

            wareSkuEntity.setStock(skuNum);

            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }


    /**
     * 批量查询sku是否有库存(请求体里面放数据->POST)
     *
     * @param skuIds skuIds
     * @return data&List_SkuHasStockVO
     */
    @Override
    public List<SkuHasStockVO> getSkusHasStock(List<Long> skuIds) {
        return skuIds.stream().map(skuId -> {
            SkuHasStockVO vo = new SkuHasStockVO();
            // fixme:这里不能是包装类型？嗯, 为long返回NULL就报错ibatis报错
            Long count = this.baseMapper.getSkuStock(skuId);
            vo.setSku_id(skuId);
            // 那么这里就可能为空
            vo.setHasStock(count != null && count > 0);
            return vo;
        }).collect(Collectors.toList());
    }

}
