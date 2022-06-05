package com.wxl.common.to.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品上架同步ES模型抽取(商品在ES中保存的数据模型)
 *
 * @author wangxl
 * @since 2022/6/5 19:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SkuESModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;

    /**
     * 销量
     */
    private Long saleCount;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * 热度评分
     */
    private Long hotScore;

    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;

    private List<Attrs> attrs;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
