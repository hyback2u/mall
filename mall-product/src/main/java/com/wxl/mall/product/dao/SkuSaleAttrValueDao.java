package com.wxl.mall.product.dao;

import com.wxl.mall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxl.mall.product.vo.SkuItemSaleAttrVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVO> getSaleAttrsBySpuId(@Param("spuId") Long spuId);
}
