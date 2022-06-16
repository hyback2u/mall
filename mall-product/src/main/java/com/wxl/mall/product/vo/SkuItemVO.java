package com.wxl.mall.product.vo;

import com.wxl.mall.product.entity.SkuImagesEntity;
import com.wxl.mall.product.entity.SkuInfoEntity;
import com.wxl.mall.product.entity.SpuInfoDescEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 商品详情-模型抽取
 *
 * @author wangxl
 * @since 2022/6/16 22:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SkuItemVO {

    // 1、sku基本信息获取 pms_sku_info
    private SkuInfoEntity info;

    // 2、sku的图片信息 pms_sku_images
    List<SkuImagesEntity> images;

    // 3、获取spu的销售属性组合
    private List<SkuItemSaleAttrVO> saleAttr;

    // 4、获取spu的介绍
    private SpuInfoDescEntity desp;

    // 5、获取spu的规格参数信息
    private List<SpuItemAttrGroupVO> groupAttrs;

//    /**
//     * Item销售属性组合VO
//     */
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @ToString
//    public static class SkuItemSaleAttrVO {
//        private Long attrId;
//        private String attrName;
//        private List<String> attrValues;
//    }


//    /**
//     * Spu基本属性VO
//     */
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @ToString
//    public static class SpuItemAttrGroupVO {
//        private String groupName;
//        private List<SpuBaseAttrVO> attrs;
//    }

//    /**
//     * 属性&属性值
//     */
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @ToString
//    public static class SpuBaseAttrVO {
//        private String attrName;
//        private String attrValue;
//    }
}
