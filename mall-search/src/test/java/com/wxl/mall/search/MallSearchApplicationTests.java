package com.wxl.mall.search;

import com.alibaba.fastjson.JSON;
import com.wxl.mall.search.config.MallElasticSearchConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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
