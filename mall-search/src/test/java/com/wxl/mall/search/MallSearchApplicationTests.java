package com.wxl.mall.search;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
