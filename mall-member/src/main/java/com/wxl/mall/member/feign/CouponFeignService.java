package com.wxl.mall.member.feign;

import com.wxl.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * FeignClient:声明式远程调用。这是一个远程客户端, 要调用远程服务:mall-coupon
 *
 * @author wangxl
 * @since 2022/5/2 3:18
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    /**
     * 如果我们以后调用该接口的memberCoupons()方法, 就会先去注册中心
     * 中先找mall-coupon所在的位置, 然后再去调用/coupon/coupon/member/list请求对应的方法
     */
    @RequestMapping(value = "/coupon/coupon/member/list")
    R memberCoupons();
}
