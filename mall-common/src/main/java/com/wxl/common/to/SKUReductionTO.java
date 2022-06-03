package com.wxl.common.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保存sku优惠、满减等信息
 *
 * @author wangxl
 * @since 2022/6/3 14:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SKUReductionTO {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
