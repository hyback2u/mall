package com.wxl.mall.search.controller;

import com.alibaba.fastjson.JSON;
import com.wxl.common.exception.BizCodeEnum;
import com.wxl.common.to.es.SkuESModel;
import com.wxl.common.utils.R;
import com.wxl.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangxl
 * @since 2022/6/5 21:51
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSearchSaveController {

    @Resource
    private ProductSaveService productSaveService;

    /**
     * 上架商品
     *
     * @param skuESModels data
     * @return message
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuESModel> skuESModels) {
        boolean statusUp;

        try {
            statusUp = productSaveService.productStatusUp(skuESModels);
        } catch (Exception e) {
            log.error("************ElasticSearchSaveController, 商品上架错误: {}", JSON.toJSONString(e));
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if (statusUp) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
