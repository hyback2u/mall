package com.wxl.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 删除属性与属性分组的关联关系时的入参
 *
 * @author wangxl
 * @since 2022/5/31 21:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttrGroupRelationVO {

    /**
     * 属性id
     */
    private Long attrId;

    /**
     * 属性分组id
     */
    private Long attrGroupId;
}
