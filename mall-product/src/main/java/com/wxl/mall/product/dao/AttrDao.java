package com.wxl.mall.product.dao;

import com.wxl.mall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    /**
     * 在指定的所有属性集合中, 挑出检索属性
     *
     * @param attrIds attrIds
     * @return attrIds_Searchable
     */
    List<Long> selectSearchAttrIds(@Param("attrIds") List<Long> attrIds);
}
