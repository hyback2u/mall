package com.wxl.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.to.SKUReductionTO;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:07:04
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存sku优惠、满减等信息
     *
     * @param skuReductionTO skuReductionTO
     */
    void saveSkuReduction(SKUReductionTO skuReductionTO);
}

