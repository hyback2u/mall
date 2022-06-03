package com.wxl.mall.ware.feign;

import com.wxl.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * pms商品系统远程接口
 *
 * @author wangxl
 * @since 2022/6/3 23:43
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    /**
     * FeignService的两种写法
     * 1、/product/skuinfo/info/{skuId} --- (直接让后台指定服务处理)
     * 2、/api/product/skuinfo/info/{skuId} --- (让所有请求过网关)
     * 要让所有的请求过网关
     * 1)@FeignClient("mall-gateway"), 给网关所在的机器发请求
     * 2)配置请求路径, 带有api
     *
     *
     * @param skuId skuId
     * @return x
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
