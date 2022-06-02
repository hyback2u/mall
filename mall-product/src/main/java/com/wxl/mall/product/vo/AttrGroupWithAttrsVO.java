package com.wxl.mall.product.vo;

import com.wxl.mall.product.entity.AttrEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 获取分类下所有分组&关联属性 -> 抽取VO
 *
 * @author wangxl
 * @since 2022/6/3 0:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttrGroupWithAttrsVO {
    /**
     * 分组id
     */
    private Long attrGroupId;

    /**
     * 组名
     */
    private String attrGroupName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String descript;

    /**
     * 组图标
     */
    private String icon;

    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 分组下的所有属性, 配合前端字段设置为attrs
     */
    private List<AttrEntity> attrs;
}
