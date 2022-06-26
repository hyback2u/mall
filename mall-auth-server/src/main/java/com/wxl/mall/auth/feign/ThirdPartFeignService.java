package com.wxl.mall.auth.feign;

import com.wxl.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangxl
 * @since 2022/6/26 13:25
 */
@FeignClient("mall-third-party")
public interface ThirdPartFeignService {

    @GetMapping("/sms/sendCode")
    R sendCode(@RequestParam("mobilePhone") String mobilePhone,
                      @RequestParam("verificationCode") String verificationCode);
}
