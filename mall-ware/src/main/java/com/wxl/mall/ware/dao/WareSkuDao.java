package com.wxl.mall.ware.dao;

import com.wxl.mall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:30:26
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * 成功采购的采购单入库
     * note: 多个参数, 一定要为每个参数指定param
     *
     * @param skuId  skuId
     * @param wareId wareId
     * @param skuNum skuNum
     */
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    /**
     * 查询指定sku的库存总数
     *
     * @param skuId skuId
     * @return long
     */
    long getSkuStock(@Param("skuId") Long skuId);
}
