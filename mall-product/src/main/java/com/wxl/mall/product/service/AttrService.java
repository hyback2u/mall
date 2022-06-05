package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.AttrEntity;
import com.wxl.mall.product.vo.AttrGroupRelationVO;
import com.wxl.mall.product.vo.AttrResponseVO;
import com.wxl.mall.product.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存, 这里入参修改为VO, 为了能够多收集一些特定的需要的信息
     *
     * @param attr AttrVO
     */
    void saveAttrVO(AttrVO attr);

    /**
     * 获取分类下的规格参数功能
     *
     * @param params    封装的查询参数
     * @param attrType  0-销售属性，1-基本属性
     * @param catelogId 分类id
     * @return PageUtils
     */
    PageUtils queryBaseAttrPage(Map<String, Object> params, String attrType, Long catelogId);

    /**
     * 根据属性实体id, 查询基础信息后组装所在分类全路径
     *
     * @param attrId 属性实体id
     * @return 关联一些其它信息后以AttrResponseVO返回
     */
    AttrResponseVO getAttrInfo(Long attrId);


    /**
     * 修改
     *
     * @param attrResponseVO attrResponseVO
     */
    void updateAttr(AttrResponseVO attrResponseVO);


    /**
     * 获取属性分组的关联的所有属性
     *
     * @param attrgroupId 属性分组id
     * @return AttrEntityList
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    /**
     * 删除属性与分组的关联关系
     *
     * @param vos vos 数组, 提交过来的是多个值(因为, 既有单个, 也有批量, 批量兼容单个)
     */
    void deleteRelation(AttrGroupRelationVO[] vos);

    /**
     * 获取属性分组没有关联的其他属性
     *
     * @param params      分页参数
     * @param attrgroupId 属性分组id
     * @return page
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性集合中, 挑出检索属性
     *
     * @param attrIds attrIds
     * @return attrIds_Searchable
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

