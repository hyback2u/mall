package com.wxl.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.wxl.common.to.es.SkuESModel;
import com.wxl.mall.search.config.MallElasticSearchConfig;
import com.wxl.mall.search.constant.ESConstant;
import com.wxl.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangxl
 * @since 2022/6/5 21:55
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuESModel> skuESModels) throws IOException {
        // 保存到 ES
        // 1、给es中建立索引 product, 建立好映射关系(Kibana操作)
        // 2、给es中保存这些数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuESModel model : skuESModels) {
            // 1、构造保存请求
            IndexRequest indexRequest = new IndexRequest(ESConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());

            String jsonString = JSON.toJSONString(model);
            indexRequest.source(jsonString, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, MallElasticSearchConfig.COMMON_OPTIONS);

        // todo 如果批量错误, 还可以在这里处理错误
        boolean hasFailures = bulk.hasFailures();
        if (hasFailures) {
            List<String> list = Arrays.stream(bulk.getItems()).map(BulkItemResponse::getId).collect(Collectors.toList());
            log.error("*******商品上架错误：{}", JSON.toJSONString(list));
        }

        return hasFailures;
    }
}
