package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.BrandEntity;
import com.wxl.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
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

    /**
     * 分类实例更新后, 更新关联的冗余表
     *
     * @param catId 分类实例id
     * @param name  分类更新后的名称
     */
    void updateCategory(Long catId, String name);


    /**
     * 根据三级分类id查询关联的所有品牌
     *
     * @param catId 三级分类id
     * @return BrandEntity数据集合
     */
    List<BrandEntity> getBrandsByCatelogId(Long catId);
}

