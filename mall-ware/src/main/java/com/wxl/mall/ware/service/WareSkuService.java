package com.wxl.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.to.SkuHasStockVO;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:30:26
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 成功采购的采购单入库
     *
     * @param skuId  skuId
     * @param wareId wareId
     * @param skuNum skuNum
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);


    /**
     * 批量查询sku是否有库存(请求体里面放数据->POST)
     *
     * @param skuIds skuIds
     * @return data&List_SkuHasStockVO
     */
    List<SkuHasStockVO> getSkusHasStock(List<Long> skuIds);
}

