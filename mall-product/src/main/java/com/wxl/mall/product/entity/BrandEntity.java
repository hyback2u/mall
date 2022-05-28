package com.wxl.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wxl.common.valid.AddGroup;
import com.wxl.common.valid.ShowStatusValidated;
import com.wxl.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(message = "修改品牌实体必须指定id", groups = {UpdateGroup.class})
    @Null(message = "新增品牌实体不能指定id", groups = {AddGroup.class})
    @TableId
    private Long brandId;

    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    /**
     * 品牌logo地址
     * URL注解(hibernate提供), 可以校验该字段必须是URL地址
     */
    @NotEmpty(message = "品牌logo地址不能为空", groups = AddGroup.class)
    @URL(message = "logo必须是合法的URL地址", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;

    /**
     * 介绍
     */
    private String descript;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    @ShowStatusValidated(value = {0, 1}, groups = {AddGroup.class})
    @NotNull(message = "显示状态不能为空", groups = AddGroup.class)
    private Integer showStatus;

    /**
     * 检索首字母
     */
    @NotEmpty(message = "检索首字母不能为空", groups = AddGroup.class)
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个字母", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = AddGroup.class)
    @Min(value = 0, message = "排序必须大于等于0", groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
