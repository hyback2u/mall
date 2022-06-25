package com.wxl.mall.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面登录注册用
 *
 * @author wangxl
 * @since 2022/6/25 23:41
 */
@Controller
public class LoginController {

    /**
     * 跳转到登录页面
     *
     * @return login.html
     */
    @GetMapping("/login.html")
    public String loginPage() {
        return "login";
    }


    /**
     * 跳转到注册页面
     *
     * @return reg.html
     */
    @GetMapping("/reg.html")
    public String regPage() {
        return "reg";
    }
}
