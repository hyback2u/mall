package com.wxl.mall.product.dao;

import com.wxl.mall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    /**
     * 上架接口远程调用成功后, 修改SPU状态
     *
     * @param spuId spuId
     * @param code 状态code
     */
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") Integer code);
}
