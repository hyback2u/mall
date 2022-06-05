package com.wxl.common.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * sku是否有库存返回模型
 *
 * @author wangxl
 * @since 2022/6/5 20:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SkuHasStockVO {
    private Long sku_id;
    private Boolean hasStock;
}
