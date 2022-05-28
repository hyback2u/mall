package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 如果没有三级分类, 就传0, 查询所有; 有的话就查三级分类下的
     *
     * @param params 分页参数
     * @param categoryId 三级分类id
     * @return message&data
     */
    PageUtils queryPage(Map<String, Object> params, Long categoryId);
}

