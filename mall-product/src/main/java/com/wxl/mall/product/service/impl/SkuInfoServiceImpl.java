package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.SkuInfoDao;
import com.wxl.mall.product.entity.SkuInfoEntity;
import com.wxl.mall.product.service.SkuInfoService;
import com.wxl.mall.product.vo.SkuItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<>());

        return new PageUtils(page);
    }


    /**
     * sku检索
     *
     * @param params params
     * @return pageData
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and((wrapper) -> wrapper.eq("sku_id", key)
                    .or().like("sku_name", key));
        }

        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("catalog_id", catelogId);
        }

        // TODO 这里标准的话用, BigDecimal
        String min = (String) params.get("min");
        log.info("********input params: min: {}", min);
        if (StringUtils.isNotBlank(min) && !"0".equalsIgnoreCase(min)) {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        log.info("********input params: max: {}", max);
        if (StringUtils.isNotBlank(max) && !"0".equalsIgnoreCase(max)) {
            queryWrapper.le("price", max);
        }

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }


    /**
     * 查询spu下所有的sku信息
     *
     * @param spuId spuId
     * @return skuList
     */
    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        return this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
    }


    /**
     * 商品详情
     *
     * @param skuId skuId
     * @return 商品详情模型数据
     */
    @Override
    public SkuItemVO item(Long skuId) {
        SkuItemVO skuItemVO = new SkuItemVO();

        // 1、sku基本信息获取 pms_sku_info

        // 2、sku的图片信息 pms_sku_images

        // 3、获取spu的销售属性组合

        // 4、获取spu的介绍

        // 5、获取spu的规格参数信息



        return skuItemVO;
    }

}
