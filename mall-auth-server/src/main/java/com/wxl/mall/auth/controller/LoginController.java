package com.wxl.mall.auth.controller;

import com.wxl.common.constant.AuthServerConstant;
import com.wxl.common.exception.BizCodeEnum;
import com.wxl.common.utils.R;
import com.wxl.mall.auth.config.MallWebConfig;
import com.wxl.mall.auth.feign.ThirdPartFeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("mobilePhone") String mobilePhone) {
        String verificationCode = UUID.randomUUID().toString().substring(0, 5) + "_" + System.currentTimeMillis();

        // 1、接口防刷 todo

        // 2、验证码60s
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + mobilePhone);
        if (StringUtils.isNoneBlank(redisCode)) {
            assert redisCode != null;
            long sendAt = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - sendAt < 60000) {
                // 60s内不能再发送
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        // 3、验证码的再次校验(注册时), 存入redis k->sms:code:手机号, v-验证码
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + mobilePhone, verificationCode,
                5, TimeUnit.MINUTES);

        thirdPartFeignService.sendCode(mobilePhone, verificationCode);
        log.info("mobilePhone = {}, verificationCode = {}", mobilePhone, verificationCode);
        return R.ok();
    }
}
