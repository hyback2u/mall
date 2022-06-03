package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据spuId进行回显属性, 获取spu规格参数
     *
     * @param spuId spuId
     * @return data
     */
    List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId);

    /**
     * 更新spu的基本属性
     *
     * @param spuId    spuId
     * @param entities 基本属性List
     */
    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}

