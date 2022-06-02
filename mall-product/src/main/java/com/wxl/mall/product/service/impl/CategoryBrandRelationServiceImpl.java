package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.BrandDao;
import com.wxl.mall.product.dao.CategoryBrandRelationDao;
import com.wxl.mall.product.dao.CategoryDao;
import com.wxl.mall.product.entity.BrandEntity;
import com.wxl.mall.product.entity.CategoryBrandRelationEntity;
import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private BrandDao brandDao;

    @Resource
    private CategoryDao categoryDao;

    // Service相比于Dao(自动生成)来说, 功能更加丰富
//    @Resource
//    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(new Query<CategoryBrandRelationEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }

    /**
     * 新增品牌与分类的关联关系
     *
     * @param categoryBrandRelation 关联关系
     */
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        // 1、查询详细名字
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        // 2、塞值
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        // 3、调用保存 AR
        this.save(categoryBrandRelation);
    }

    /**
     * 品牌实体更改后, 进行关联的表冗余信息更新
     *
     * @param brandId 更新了品牌名的品牌实体id
     * @param name    修改后的品牌名
     */
    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setBrandId(brandId);
        categoryBrandRelation.setBrandName(name);

        // new UpdateWrapper<>():更新条件, 即更新表中所有的品牌id等于这个的数据
        this.update(categoryBrandRelation, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
    }

    /**
     * 分类实例更新后, 更新关联的品牌分类关系表
     *
     * @param catId 分类实例id
     * @param name  分类更新后的名称
     */
    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId, name);
    }


    /**
     * 根据三级分类id查询关联的所有品牌
     *
     * @param catId 三级分类id
     * @return BrandEntity数据集合
     */
    @Override
    public List<BrandEntity> getBrandsByCatelogId(Long catId) {
        List<CategoryBrandRelationEntity> relationEntities = this.list(new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("catelog_id", catId));
        List<Long> brandIds = relationEntities.stream().map(CategoryBrandRelationEntity::getBrandId).collect(Collectors.toList());

        // 这里其实从关系表中就可以拿到, 但是为了通用性, 还是返回所有详细信息
        return brandDao.selectBatchIds(brandIds);
    }

}
