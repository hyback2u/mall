package com.wxl.mall.product.feign;

import com.wxl.common.to.SKUReductionTO;
import com.wxl.common.to.SPUBoundsTO;
import com.wxl.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SpringCloud远程调用逻辑梳理回顾
 * 1、假设有一个Service调用了CouponFeignService的saveSPUBounds方法, 还传了一个对象
 * 1.1 SpringCloud将这个对象转为Json, 这就需要@RequestBody注解
 * 1.2 SpringCloud给去注册中心找到mall-coupon远程服务, 给该服务的/coupon/spubounds/save发送请求
 * 会将上一步转的Json放到请求体位置发送请求
 * 1.3 对方服务收到请求, 请求体里有Json数据, 对方服务又写了一个save(@RequestBody XX...
 * 就是将请求体的Json转为XX类型, 能转(属性名对应)
 * <p>
 * 总结:只要Json数据模型是兼容的, 双方服务无需使用同一个TO
 *
 * @author wangxl
 * @since 2022/6/3 13:50
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    /**
     * 保存SPU时, 调用远程接口保存SPU对应的优惠信息
     *
     * @param spuBoundsTO spuBoundsTO
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSPUBounds(@RequestBody SPUBoundsTO spuBoundsTO);


    /**
     * 保存sku优惠、满减等信息
     *
     * @param skuReductionTO skuReductionTO
     * @return message
     */
    @PostMapping("/coupon/skufullreduction/saveReduction")
    R saveSKUReduction(@RequestBody SKUReductionTO skuReductionTO);
}
