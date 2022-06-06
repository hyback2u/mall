package com.wxl.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 前端使用, 二级分类VO
 *
 * @author wangxl
 * @since 2022/6/6 21:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Catelog2VO {

    /**
     * 一级父分类id
     */
    private String catalog1Id;

    /**
     * 三级子分类
     */
    private List<Catelog3VO> catalog3List;

    /**
     * 当前节点id
     */
    private String id;

    /**
     * 当前节点名称
     */
    private String name;

    /**
     * 三级分类 VO(静态内部类)
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Catelog3VO {

        /**
         * 父分类id(二级分类id)
         */
        private String catalog2Id;

        /**
         * 当前分类id
         */
        private String id;

        /**
         * 当前分类名称
         */
        private String name;
    }

}
