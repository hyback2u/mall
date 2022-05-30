package com.wxl.mall.product.dao;

import com.wxl.mall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    /**
     * 分类实例更新后, 更新关联的品牌分类关系表
     *
     * @param catId 分类实例id
     * @param name  分类更新后的名称
     */
    void updateCategory(@Param("catId") Long catId, @Param("name") String name);
}
