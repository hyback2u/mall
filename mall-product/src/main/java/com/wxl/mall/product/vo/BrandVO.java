package com.wxl.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 品牌返回VO
 *
 * @author wangxl
 * @since 2022/6/2 22:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BrandVO {
    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 品牌名
     */
    private String brandName;
}
