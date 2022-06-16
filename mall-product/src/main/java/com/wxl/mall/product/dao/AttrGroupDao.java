package com.wxl.mall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxl.mall.product.entity.AttrGroupEntity;
import com.wxl.mall.product.vo.SpuItemAttrGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    /**
     * 组合查询
     *
     * @param spuId     spuId
     * @param catalogId 三级分类id
     * @return SkuItemVO.SpuItemAttrGroupVO
     */
    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
