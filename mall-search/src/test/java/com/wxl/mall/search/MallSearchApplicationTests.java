package com.wxl.mall.search;

import com.alibaba.fastjson.JSON;
import com.wxl.mall.search.config.MallElasticSearchConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author wangxl
 * @since 2022/6/5 13:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSearchApplicationTests {
    @Autowired
    public RestHighLevelClient client;

    @Test
    public void contextLoads() {
        System.out.println("*************client: " + client);
    }


    /**
     * 测试复杂检索功能
     * 检索地址中带有 mill 的人员年龄分布和平均薪资
     */
    @Test
    public void complexIndexTest() throws Exception {
        // 1、创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("bank");
        // 指定DSL, 检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 构造检索条件
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
//        searchSourceBuilder.aggregation();


        searchRequest.source(searchSourceBuilder);

        System.out.println("**********检索条件: " + searchSourceBuilder);

        // 2、执行检索
        SearchResponse searchResponse = client.search(searchRequest, MallElasticSearchConfig.COMMON_OPTIONS);

        // 3、分析结果
        System.out.println("***********分析结果: " + searchResponse.toString());
    }


    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class User {
        private String userName;
        private String gender;
        private Integer age;
    }

    /**
     * 测试给es中存储数据
     * 保存、更新 二合一
     */
    @Test
    public void indexTest() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1"); // 数据的id
//        indexRequest.source("username", "zhangSan", "age", 18, "gender", "男");
        User user = new User("zhangsan", "man", 18);
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON); // 要保存的内容

        // 执行操作
        IndexResponse response = client.index(indexRequest, MallElasticSearchConfig.COMMON_OPTIONS);

        // 提取有用的响应数据
        System.out.println("***************response: " + response);
    }


}
