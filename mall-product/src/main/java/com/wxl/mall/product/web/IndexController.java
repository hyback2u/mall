package com.wxl.mall.product.web;

import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.service.CategoryService;
import com.wxl.mall.product.vo.Catelog2VO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 首页页面跳转Controller
 *
 * @author wangxl
 * @since 2022/6/6 20:22
 */
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;


    /**
     * 压测简单服务
     *
     * @return 压力测试
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


    /**
     * 首页跳转, 渲染一级分类数据
     *
     * @return classpath:/templates/xx.html
     */
    @GetMapping({"/", "index.html"})
    public String indexPage(Model model) {

        // 1、查出所有的一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntityList);

        return "index";
    }


    /**
     * [前端]查出所有分类, 按照形式组织后返回
     *
     * @return data
     */
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2VO>> getCatalogJson() {
//        return categoryService.getCatalogJson();
//        return categoryService.getCatalogJsonPlus();
        return categoryService.getCatalogJsonPlusPro();
    }
}
