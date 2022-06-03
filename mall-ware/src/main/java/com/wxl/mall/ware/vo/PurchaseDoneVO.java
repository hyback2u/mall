package com.wxl.mall.ware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 完成采购单VO
 *
 * @author wangxl
 * @since 2022/6/3 23:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseDoneVO {

    /**
     * 采购单id
     */
    private Long id;

    /**
     * 采购项
     */
    private List<PurchaseItemDoneVO> items;

}
