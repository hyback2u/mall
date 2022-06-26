package com.wxl.mall.thirdparty.controller;

import com.wxl.common.utils.R;
import com.wxl.mall.thirdparty.component.SmsComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangxl
 * @since 2022/6/26 12:03
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Resource
    private SmsComponent smsComponent;

    /**
     * 提供给别的服务来调用的, 而不是提供给页面直接发请求的
     *
     * @param mobilePhone      目标手机号
     * @param verificationCode 验证码
     * @return R
     */
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("mobilePhone") String mobilePhone,
                      @RequestParam("verificationCode") String verificationCode) {

        smsComponent.sendSmsCode(mobilePhone, verificationCode);
        log.info("******thirdService: mobilePhone = {}, verificationCode = {}", mobilePhone, verificationCode);
        return R.ok();
    }

}
