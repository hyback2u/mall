package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.SpuInfoEntity;
import com.wxl.mall.product.vo.SPUSaveVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存页面发布商品SPU信息, 一个大的业务逻辑保存功能
     *
     * @param spuSaveVO formData
     */
    void saveSPUInfo(SPUSaveVO spuSaveVO);


    /**
     * 保存SPU的基本信息
     *
     * @param spuInfoEntity spuInfoEntity
     */
    void saveBaseSPUInfo(SpuInfoEntity spuInfoEntity);

    /**
     * spu检索
     *
     * @param params params
     * @return pageData
     */
    PageUtils queryPageByCondition(Map<String, Object> params);


    /**
     * 商品上架
     *
     * @param spuId spuId
     */
    void up(Long spuId);
}

