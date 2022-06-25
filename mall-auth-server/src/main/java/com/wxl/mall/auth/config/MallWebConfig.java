package com.wxl.mall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 视图映射
 *
 * @author wangxl
 * @since 2022/6/26 0:27
 */
@Configuration
public class MallWebConfig implements WebMvcConfigurer {

    /**
     * 视图映射
     * 仅仅是页面的跳转, 之前的用法：
     *
     * <pre> {@code
     *  @GetMapping("/login.html")
     *  public String loginPage() {
     *      return "login";
     *  }
     * }</pre>
     *
     * urlPath:相当于RequestMapping的内容
     * viewName:方法的返回值
     *
     * @param registry registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 登录页面 & 注册页面的页面跳转逻辑, 由视图映射完成
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
