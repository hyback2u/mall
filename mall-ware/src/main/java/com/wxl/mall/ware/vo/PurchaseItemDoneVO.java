package com.wxl.mall.ware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 完成采购单, 采购项VO
 *
 * @author wangxl
 * @since 2022/6/3 23:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseItemDoneVO {

    /**
     * 采购项id
     */
    private Long itemId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 原因
     */
    private String reason;
}
