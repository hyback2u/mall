package com.wxl.mall.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 检索查询参数模型(封装页面所有可能传递过来的查询条件)
 *
 * @author wangxl
 * @since 2022/6/14 21:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchParam {


    /**
     * 页面传递过来的检索关键字
     * -> 全文匹配关键字
     */
    private String keyword;


    /**
     * 三级分类id
     */
    private Long catalog3Id;


    /**
     * 是否仅显示有货
     */
    private Integer hasStock;


    /**
     * 价格区间查询
     */
    private String skuPrice;


    /**
     * 品牌(支持多选)
     */
    private List<Long> brandId;


    /**
     * 按照属性进行筛选
     */
    private List<String> attrs;


    /**
     * 页码
     */
    private Integer pageNum;
}
