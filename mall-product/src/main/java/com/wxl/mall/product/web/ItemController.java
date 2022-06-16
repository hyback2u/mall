package com.wxl.mall.product.web;

import com.wxl.mall.product.service.SkuInfoService;
import com.wxl.mall.product.vo.SkuItemVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * 商品详情
 *
 * @author wangxl
 * @since 2022/6/16 22:46
 */
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
    public String skuItem(@PathVariable("skuId") Long skuId) {

        System.out.println("准备查询skuId = " + skuId + " 的商品详情...");
        SkuItemVO skuItemVO = skuInfoService.item(skuId);

        return "item";
    }
}
