package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.BrandDao;
import com.wxl.mall.product.entity.BrandEntity;
import com.wxl.mall.product.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

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
