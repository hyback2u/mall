package com.wxl.mall.ware.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author wangxl
 * @since 2022/6/3 21:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MergeVO {
    /**
     * 采购单id
     */
    private Long purchaseId;

    /**
     * 采购项ids
     */
    private List<Long> items;
}
