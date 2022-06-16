package com.wxl.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Item销售属性组合VO
 *
 * @author wangxl
 * @since 2022/6/16 23:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SkuItemSaleAttrVO {
    private Long attrId;
    private String attrName;
    private List<String> attrValues;
}
