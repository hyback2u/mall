package com.wxl.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:25:57
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

