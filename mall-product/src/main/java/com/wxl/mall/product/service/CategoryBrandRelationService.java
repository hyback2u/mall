package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 新增品牌与分类的关联关系
     *
     * @param categoryBrandRelation 关联关系
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 品牌实体更改后, 进行关联的表冗余信息更新
     *
     * @param brandId 更新了品牌名的品牌实体id
     * @param name    修改后的品牌名
     */
    void updateBrand(Long brandId, String name);
}

