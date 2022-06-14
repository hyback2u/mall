package com.wxl.mall.search.vo;

import com.wxl.common.to.es.SkuESModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 检索返回结果模型
 *
 * @author wangxl
 * @since 2022/6/14 21:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchResult {

    /**
     * 查询到的所有商品信息
     */
    private List<SkuESModel> products;


    // ------------以下是分页信息------------
    private Integer pageNum; // 当前页码
    private Long total; // 总记录数
    private Integer totalPages; // 总页码
    // -------------------------------------


    /**
     * 当前查询结果, 所有涉及到的品牌
     */
    private List<BrandVO> brands;

    /**
     * 当前查询结果, 所涉及到的所有属性
     */
    private List<AttrVO> attrs;

    /**
     * 当前查询结果, 所有涉及到的所有品牌
     */
    private List<CatalogVO> catalogs;

    // ------------------- 以上是返回给页面的所有信息 -------------------


    /**
     * 固定属性:品牌
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class BrandVO {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }


    /**
     * 计算属性
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class AttrVO {
        private Long attrId;
        private String attrName;

        private List<String> attrValue;
    }

    /**
     * 固定属性:分类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private  static class CatalogVO {
        private Long catalogId;
        private String catalogName;
    }
}
