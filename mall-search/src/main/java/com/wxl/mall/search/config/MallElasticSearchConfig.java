package com.wxl.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式商城, 检索服务配置
 * 1、导入依赖
 * 2、编写配置, 给容器中注入RestHighLevelClient
 * 3、参考官方api进行开发 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html
 *
 * @author wangxl
 * @since 2022/6/5 13:15
 */
@Configuration
public class MallElasticSearchConfig {

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.186.129", 9200, "http")));
    }
}
