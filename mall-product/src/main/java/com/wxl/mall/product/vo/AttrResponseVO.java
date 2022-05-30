package com.wxl.mall.product.vo;

import lombok.*;

/**
 * 响应自获取分类下的规格参数功能, 添加多余的字段供前端渲染
 *
 * @author wangxl
 * @since 2022/5/30 23:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttrResponseVO extends AttrVO {

    /**
     * 所属分类名称
     */
    private String catelogName;

    /**
     * 所属分组
     */
    private String groupName;
}
