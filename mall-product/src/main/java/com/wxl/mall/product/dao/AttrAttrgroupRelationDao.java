package com.wxl.mall.product.dao;

import com.wxl.mall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    /**
     * 批量删除属性&属性分组关联
     *
     * @param relationEntities 属性&属性分组关联实体数组
     */
    void deleteBatchRelation(@Param("relationEntities") List<AttrAttrgroupRelationEntity> relationEntities);
}
