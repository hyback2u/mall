package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 在更新品牌实例的同时, 更新关联的所有的冗余表, 保证冗余字段的数据一致
     *
     * @param brand brand实例
     */
    void updateDetail(BrandEntity brand);
}

