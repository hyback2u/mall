package com.wxl.mall.product.feign;

import com.wxl.common.to.es.SkuESModel;
import com.wxl.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wangxl
 * @since 2022/6/5 22:11
 */
@FeignClient("mall-search")
public interface SearchFeignService {

    /**
     * 上架商品
     *
     * @param skuESModels data
     * @return message
     */
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuESModel> skuESModels);
}
