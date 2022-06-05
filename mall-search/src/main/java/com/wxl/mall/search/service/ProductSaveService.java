package com.wxl.mall.search.service;

import com.wxl.common.to.es.SkuESModel;

import java.io.IOException;
import java.util.List;

/**
 * @author wangxl
 * @since 2022/6/5 21:54
 */
public interface ProductSaveService {


    boolean productStatusUp(List<SkuESModel> skuESModels) throws IOException;
}
