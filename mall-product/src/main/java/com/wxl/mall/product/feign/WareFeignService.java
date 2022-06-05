package com.wxl.mall.product.feign;

import com.wxl.common.to.SkuHasStockVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 库存Feign调用接口
 *
 * @author wangxl
 * @since 2022/6/5 21:30
 */
@FeignClient("mall-ware")
public interface WareFeignService {

    /**
     * 批量查询sku是否有库存(请求体里面放数据->POST)
     * 远程调用的接口, 为了解决返回值类型的问题, 有如下解决方案
     * 1、R设计的时候, 加上泛型 √
     * 2、直接返回想要的结果
     * 3、自己封装返回结果
     *
     * fixme:这里我使用的是方式2
     *
     * @param skuIds skuIds
     * @return data&List_SkuHasStockVO
     */
    @PostMapping("/ware/waresku/hasStock")
    List<SkuHasStockVO> getSkusHasStock(@RequestBody List<Long> skuIds);
}
