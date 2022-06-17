package com.wxl.mall.product.web;

import com.alibaba.fastjson.JSON;
import com.wxl.mall.product.service.SkuInfoService;
import com.wxl.mall.product.vo.SkuItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 商品详情
 *
 * @author wangxl
 * @since 2022/6/16 22:46
 */
@Slf4j
@Controller
public class ItemController {

    @Resource
    private SkuInfoService skuInfoService;

    /**
     * 商品详情页, 展示当前sku的详情
     *
     * @param skuId skuId
     * @return item
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {

        System.out.println("准备查询skuId = " + skuId + " 的商品详情...");
        SkuItemVO skuItemVO = skuInfoService.item(skuId);
        log.info("************skuItemVO info: {}", JSON.toJSONString(skuItemVO));
        model.addAttribute("item", skuItemVO);

        return "item";
    }
}
