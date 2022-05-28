package com.wxl.mall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wangxl
 * @since 2022/5/28 9:45
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallThirdPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallThirdPartyApplication.class, args);
    }
}
