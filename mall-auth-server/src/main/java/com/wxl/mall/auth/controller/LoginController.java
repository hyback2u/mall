package com.wxl.mall.auth.controller;

import com.wxl.common.utils.R;
import com.wxl.mall.auth.config.MallWebConfig;
import com.wxl.mall.auth.feign.ThirdPartFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 页面登录注册用
 *
 * @author wangxl
 * @see MallWebConfig#addViewControllers
 * @since 2022/6/25 23:41
 */
@Slf4j
@Controller
public class LoginController {

    @Resource
    private ThirdPartFeignService thirdPartFeignService;

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("mobilePhone") String mobilePhone) {
        String verificationCode = UUID.randomUUID().toString().substring(0, 5);

        thirdPartFeignService.sendCode(mobilePhone, verificationCode);

        log.info("mobilePhone = {}, verificationCode = {}", mobilePhone, verificationCode);
        return R.ok();
    }
}
