package com.wxl.mall.order.dao;

import com.wxl.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:25:57
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
