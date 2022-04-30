package com.wxl.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.coupon.entity.SkuLadderEntity;

import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:07:04
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

