package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.vo.Catelog2VO;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查出所有分类以及子分类, 以树形结构组装起来
     *
     * @return 封装成树形结构的分类List
     */
    List<CategoryEntity> listWithTree();


    void removeConditionalByIds(List<Long> idList);

    /**
     * 根据三级分类id查询出该分类的完整路径
     *
     * @param catelogId 三级分类id
     * @return Long[], 分类的完整路径
     */
    Long[] findCatelogPathByCatelogId(Long catelogId);

    /**
     * 在更新分类实例的同时, 更新关联的所有的冗余表, 保证冗余字段的数据一致
     * [即:级联更新所有关联的数据]
     *
     * @param category 分类实例
     */
    void updateCascade(CategoryEntity category);

    /**
     * 一级分类数据
     *
     * @return data
     */
    List<CategoryEntity> getLevel1Categories();

    /**
     * [前端]查出所有分类, 按照形式组织后返回
     *
     * @deprecated after optimization
     * @see CategoryService#getCatalogJsonPlus()
     * @return data
     */
    @Deprecated
    Map<String, List<Catelog2VO>> getCatalogJson();


    /**
     * [前端]查出所有分类, 按照形式组织后返回
     * -----------------------------------
     * first optimization:减少与数据库的交互
     *
     * @deprecated optimization:引入redis缓存
     * @see CategoryService#getCatalogJsonPlusPro()
     * @return data
     */
    @Deprecated
    Map<String, List<Catelog2VO>> getCatalogJsonPlus();


    /**
     * [前端]查出所有分类, 按照形式组织后返回
     * -----------------------------------
     * final optimization:使用redis缓存中间件
     *
     * @deprecated optimization:使用spring-cache
     * @see CategoryService#getCatalogJsonWithCache()
     * @return data
     */
    @Deprecated
    Map<String, List<Catelog2VO>> getCatalogJsonPlusPro();


    /**
     * final optimization:使用spring-cache
     *
     * @return data
     */
    Map<String, List<Catelog2VO>> getCatalogJsonWithCache();
}

