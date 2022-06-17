package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.SkuInfoDao;
import com.wxl.mall.product.entity.SkuImagesEntity;
import com.wxl.mall.product.entity.SkuInfoEntity;
import com.wxl.mall.product.entity.SpuInfoDescEntity;
import com.wxl.mall.product.service.*;
import com.wxl.mall.product.vo.SkuItemSaleAttrVO;
import com.wxl.mall.product.vo.SkuItemVO;
import com.wxl.mall.product.vo.SpuItemAttrGroupVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;


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
    public SkuItemVO item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVO skuItemVO = new SkuItemVO();

        // 1、sku基本信息获取 pms_sku_info
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity info = this.getById(skuId);
            skuItemVO.setInfo(info);
            return info;
        }, threadPoolExecutor);


        // 3、4、5 均依赖 1的执行, 这里1执行完毕后, 3、4、5都可以异步执行

        // 3、获取spu的销售属性组合
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            List<SkuItemSaleAttrVO> saleAttrVOs = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVO.setSaleAttr(saleAttrVOs);
        }, threadPoolExecutor);

        // 4、获取spu的介绍
        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            SpuInfoDescEntity spuInfo = spuInfoDescService.getById(res.getSpuId());
            skuItemVO.setDesp(spuInfo);
        }, threadPoolExecutor);

        // 5、获取spu的规格参数信息
        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            List<SpuItemAttrGroupVO> attrGroupVOS = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVO.setGroupAttrs(attrGroupVOS);
        }, threadPoolExecutor);

        // 2、sku的图片信息 pms_sku_images(2和1没关系, 新开一个进行执行)
        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
            skuItemVO.setImages(images);
        }, threadPoolExecutor);

        // 等待所有的任务都完成
        CompletableFuture.allOf(saleAttrFuture, descFuture, baseAttrFuture, imagesFuture).get();

        return skuItemVO;
    }

}
