package com.wxl.mall.auth.controller;

import com.wxl.common.constant.AuthServerConstant;
import com.wxl.common.exception.BizCodeEnum;
import com.wxl.common.utils.R;
import com.wxl.mall.auth.config.MallWebConfig;
import com.wxl.mall.auth.feign.MemberFeignService;
import com.wxl.mall.auth.feign.ThirdPartFeignService;
import com.wxl.mall.auth.vo.UserRegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
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

    @Resource
    private MemberFeignService memberFeignService;

    /**
     * 注册功能
     *
     * @param vo         UserRegisterVO
     * @param result     BindingResult校验
     * @param attributes 模拟重定向携带数据
     * @return page
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVO vo, BindingResult result, RedirectAttributes attributes) {
        // 1、校验
        if (result.hasErrors()) {
            // 方便页面进行提取
            Map<String, String> errors = new HashMap<>();
            // > 这里不使用stream.toMap...
            for (FieldError fieldError : result.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            attributes.addFlashAttribute("errors", errors);

            // Request method 'POST' not supported, 转发 reg.html (路径映射默认都是get方式访问的)
            // 如果有错误, 重定向到注册页面(防止页面重复提交) ---> 这样的话, 数据肯定不行了？ 不然你重定向去百度... fixme
            return "redirect:http://auth.mall.com/reg.html";
        }

        // 2、真正注册, 调用远程服务进行注册
        // 2.1 校验验证码
        String code = vo.getCode();
        String codeFromRedis = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (StringUtils.isBlank(codeFromRedis)) {
            // 验证码过期了
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/reg.html";
        } else {
            // split
            if (codeFromRedis.split("_")[0].equals(code)) {
                // 1、删除验证码:令牌机制
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                // 2、验证码对比层成功, 调用远程服务进行注册
                R register = memberFeignService.register(vo);
                if (register.getCode().equals(0)) {
                    // 成功
                    return "redirect:/login.html";
                } else {
                    // 失败
                    Map<String, String> errors = new HashMap<>();
                    // todo, 从r中取出
                    errors.put("msg", "用户名或手机号已被占用");
                    attributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.mall.com/reg.html";
                }
            } else {
                // 验证不通过的逻辑
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码输入错误");
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.mall.com/reg.html";
            }
        }


        // 注册成功, 回到登录页面
//        return "redirect:/login.html";
    }


    /**
     * 发送验证码
     *
     * @param mobilePhone mobilePhone
     * @return R
     */
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
