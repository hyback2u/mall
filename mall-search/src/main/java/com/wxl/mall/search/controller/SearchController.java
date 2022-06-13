package com.wxl.mall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wangxl
 * @since 2022/6/14 2:24
 */
@Controller
public class SearchController {

    /**
     * 配置首页跳转
     *
     * @return 转到list.html页面
     */
    @GetMapping("/list.html")
    public String listPage() {
        return "list";
    }

}
