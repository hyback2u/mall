package com.wxl.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.product.entity.SkuInfoEntity;
import com.wxl.mall.product.vo.SkuItemVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * sku检索
     *
     * @param params params
     * @return pageData
     */
    PageUtils queryPageByCondition(Map<String, Object> params);


    /**
     * 查询spu下所有的sku信息
     *
     * @param spuId spuId
     * @return skuList
     */
    List<SkuInfoEntity> getSkusBySpuId(Long spuId);

    /**
     * 商品详情
     *
     * @param skuId skuId
     * @return 商品详情模型数据
     */
    SkuItemVO item(Long skuId) throws ExecutionException, InterruptedException;
}

