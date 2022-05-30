package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.BrandDao;
import com.wxl.mall.product.entity.BrandEntity;
import com.wxl.mall.product.service.BrandService;
import com.wxl.mall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 默认是没有任何条件直接检索所有, 现在需要配置模糊查询
     *
     * @param params params
     * @return PageUtils
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();

        // 不为空, 说明页面要进行模糊检索
        if (StringUtils.isNotBlank(key)) {
            wrapper.eq("brand_id", key).or()
                    .like("name", key).or().like("descript", key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }


    /**
     * 在更新品牌实例的同时, 更新关联的所有的冗余表, 保证冗余字段的数据一致
     * 这里需要开启MyBatisPlus中的事务注解, 这里才能使用事务
     *
     * @param brand brand实例
     * @see com.wxl.mall.product.config.MyBatisPlusConfig 开启事务
     */
    @Override
    @Transactional
    public void updateCascade(BrandEntity brand) {
        // 保证冗余字段的数据一致
        // 1、更新自身表的数据信息
        this.updateById(brand);

        // 2、如果品牌名不为空, 进行关联的表冗余信息更新
        if (StringUtils.isBlank(brand.getName())) {
            return;
        }

        // 进行关联的表冗余信息更新
        categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());

        // todo 更新其它关联
    }

//    @Override
//    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<BrandEntity> page = this.page(
//                new Query<BrandEntity>().getPage(params),
//                new QueryWrapper<BrandEntity>()
//        );
//
//        return new PageUtils(page);
//    }

}
