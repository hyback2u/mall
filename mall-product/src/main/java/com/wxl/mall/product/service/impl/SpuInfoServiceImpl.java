package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.to.SKUReductionTO;
import com.wxl.common.to.SPUBoundsTO;
import com.wxl.common.to.SkuHasStockVO;
import com.wxl.common.to.es.SkuESModel;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.common.utils.R;
import com.wxl.mall.product.dao.SpuInfoDao;
import com.wxl.mall.product.entity.*;
import com.wxl.mall.product.feign.CouponFeignService;
import com.wxl.mall.product.feign.WareFeignService;
import com.wxl.mall.product.service.*;
import com.wxl.mall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private AttrService attrService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private WareFeignService wareFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }

    /**
     * 保存页面发布商品SPU信息, 一个大的业务逻辑保存功能
     * todo 各种异常处理、数据同步等...
     *
     * @param spuSaveVO formData
     */
    @Transactional
    @Override
    public void saveSPUInfo(SPUSaveVO spuSaveVO) {
        // 1、保存SPU基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();

        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());

        this.saveBaseSPUInfo(spuInfoEntity);

        // 2、保存SPU的描述图片 pms_spu_info_desc
        List<String> decript = spuSaveVO.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuInfoEntity.getId());
        descEntity.setDecript(String.join(",", decript));

        spuInfoDescService.saveSpuInfoDesc(descEntity);

        // 3、保存SPU的图片集 pms_spu_images
        List<String> images = spuSaveVO.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);


        // 4、保存SPU的规格参数 pms_product_attr_value、sms_spu_bounds(积分信息)
        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();
        List<ProductAttrValueEntity> attrValueEntityList = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValue = new ProductAttrValueEntity();

            productAttrValue.setAttrId(attr.getAttrId());

            AttrEntity entity = attrService.getById(attr.getAttrId());
            productAttrValue.setAttrName(entity.getAttrName());

            productAttrValue.setAttrValue(attr.getAttrValues());
            productAttrValue.setQuickShow(attr.getShowDesc());
            productAttrValue.setSpuId(spuInfoEntity.getId());

            return productAttrValue;
        }).collect(Collectors.toList());

        // todo:这里为什么不直接用, 而是再定义一个service, 调用this.saveBatch(xx)？
        productAttrValueService.saveBatch(attrValueEntityList);

        // feign
        Bounds bounds = spuSaveVO.getBounds();
        SPUBoundsTO spuBoundsTO = new SPUBoundsTO();
        BeanUtils.copyProperties(bounds, spuBoundsTO);
        spuBoundsTO.setSpuId(spuInfoEntity.getId());
        R result = couponFeignService.saveSPUBounds(spuBoundsTO);
        if (result.getCode() != 0) {
            log.error("远程保存SPU积分信息失败");
        }


        // 5、保存当前SPU对应的所有SKU信息
        List<Skus> skus = spuSaveVO.getSkus();
        if (null == skus || skus.size() == 0) {
            return;
        }

        // fixme:不能这样, 因为只有保存了才有自增id
//        List<SkuInfoEntity> skuInfoEntityList = new ArrayList<>();
        skus.forEach(sku -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());

            // 设置默认图片
            for (Images image : sku.getImages()) {
                if (image.getDefaultImg() == 1) {
                    skuInfoEntity.setSkuDefaultImg(image.getImgUrl());
                }
            }

            // 5.1 sku基本信息 pms_sku_info
            skuInfoService.save(skuInfoEntity);
            // 不同步？
//            sku.getImages().forEach(img-> {
//                if(img.getDefaultImg() == 1) {
//                    defaultImg = img;
//                }
//            });

            List<SkuImagesEntity> skuImagesEntityList = sku.getImages().stream().map(img -> {
                SkuImagesEntity skuImage = new SkuImagesEntity();

                skuImage.setSkuId(skuInfoEntity.getSkuId());
                skuImage.setImgUrl(img.getImgUrl());
                skuImage.setDefaultImg(img.getDefaultImg());

                return skuImage;
            }).filter(x -> StringUtils.isNotBlank(x.getImgUrl())).collect(Collectors.toList());

            // 5.2 sku的图片信息 pms_sku_images todo:没有图片路径的, 无需保存
            skuImagesService.saveBatch(skuImagesEntityList);

            List<Attr> attrList = sku.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = attrList.stream().map(attr -> {
                SkuSaleAttrValueEntity saleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, saleAttrValueEntity);
                saleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());

                return saleAttrValueEntity;
            }).collect(Collectors.toList());

            // 5.3 sku的销售属性信息 pms_sku_sale_attr_value
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

            // 5.4 sku的优惠、满减等信息 --> 跨库保存 mall_sms
            // 5.4.1 sms_sku_ladder 打折
            // 5.4.2 sms_sku_full_reduction 满减
            // 5.4.3 sms_member_price 会员价
            SKUReductionTO skuReductionTO = new SKUReductionTO();
            BeanUtils.copyProperties(sku, skuReductionTO);
            skuReductionTO.setSkuId(skuInfoEntity.getSkuId());
            // BigDecimal比较大小
            if (skuReductionTO.getFullCount() > 0 || skuReductionTO.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                R r = couponFeignService.saveSKUReduction(skuReductionTO);
                if (r.getCode() != 0) {
                    log.error("远程保存SKU优惠信息失败");
                }
            }
        });

    }

    @Override
    public void saveBaseSPUInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    /**
     * spu检索
     *
     * @param params params
     * @return pageData
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and((wrapper) -> wrapper.eq("id", key).or().like("spu_name", key));
        }

        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("publish_status", status);
        }


        // TODO 为啥这里的StringUtils.isNotBlank会报NPE
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }
//        Object brandId = params.get("brandId");
//        if (null != brandId && (int) brandId != 0) {
//            queryWrapper.eq("brand_id", brandId);
//        }
//

        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("catalog_id", catelogId);
        }
//        Object catelogId = params.get("catelogId");
//        if (null != catelogId && (int) catelogId != 0) {
//            queryWrapper.eq("catalog_id", catelogId);
//        }


        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }


    /**
     * 商品上架
     *
     * @param spuId spuId
     */
    @Override
    public void up(Long spuId) {
        // 1、查出当前spuId对应的所有sku信息, 包含品牌名等
        List<SkuInfoEntity> skuList = skuInfoService.getSkusBySpuId(spuId);

        // 可以被检索规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuESModel.Attrs> attrs = baseAttrs.stream()
                .filter(item -> idSet.contains(item.getAttrId()))
                .map(item -> {
                    SkuESModel.Attrs attr = new SkuESModel.Attrs();
                    BeanUtils.copyProperties(item, attr);
                    return attr;
                })
                .collect(Collectors.toList());

        // 期望远程服务有一个接口可以统一查询sku是否有库存
        List<Long> skuIds = skuList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        Map<Long, Boolean> stockMap = null;
        try {
            List<SkuHasStockVO> stockVOList = wareFeignService.getSkusHasStock(skuIds);
//        result.get("data");
            // 转为 map  <skuId, hasStock>
            stockMap = stockVOList.stream().collect(Collectors.toMap(
                    SkuHasStockVO::getSku_id, SkuHasStockVO::getHasStock
            ));
        } catch (Exception e) {
            log.error("*********WareFeignService Exception: {}", e);
        }


        // 2、封装每个sku的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuESModel> upProducts = skuList.stream().map(sku -> {
            SkuESModel esModel = new SkuESModel();
            BeanUtils.copyProperties(sku, esModel);

            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());
            // 判断是否有库存 1、发送远程调用, 库存系统查询是否有库存
            // fixme:这里循环调用远程库存查询, 是一个很慢、很耗时的过程, 这里优化为期望远程服务有一个接口可以统一查询sku是否有库存

            // 这里, 分布式调用失败的处理方法默认有库存
            if(null == finalStockMap) {
                esModel.setHasStock(true);
            } else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            // 2、热度评分(例如, 这里先设置刚上架的是0, ps:这块应该是后台可控的非常复杂的一个操作)
            esModel.setHotScore(0L);

            BrandEntity brandEntity = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandImg(brandEntity.getLogo());

            CategoryEntity categoryEntity = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(categoryEntity.getName());

            // 3、设置检索属性, 查询当前sku所有的可以被检索规格属性 --> 按照spu来的, 放在外边
            esModel.setAttrs(attrs);

            return esModel;
        }).collect(Collectors.toList());

        // 3、发送给ElasticSearch进行保存 交给mall-search处理


    }
}
