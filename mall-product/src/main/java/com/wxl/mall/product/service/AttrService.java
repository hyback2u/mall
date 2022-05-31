package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.AttrEntity;
import com.wxl.mall.product.vo.AttrResponseVO;
import com.wxl.mall.product.vo.AttrVO;

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
     * @param attrType 0-销售属性，1-基本属性
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
}

