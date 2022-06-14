package com.wxl.mall.search.controller;

import com.wxl.mall.search.service.MallSearchService;
import com.wxl.mall.search.vo.SearchParam;
import com.wxl.mall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author wangxl
 * @since 2022/6/14 2:24
 */
@Controller
public class SearchController {

    @Resource
    private MallSearchService mallSearchService;

    /**
     * SpringMVC, 自动将页面提交过来的所有请求查询参数封装成指定的对象
     *
     * @return xxx
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model) {
        // 根据传递过来的页面的查询参数, 去ES中检索商品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);

        return "list";
    }

}
