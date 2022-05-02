package com.wxl.mall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 使用nacos作为配置中心统一管理配置
 * 1、引入依赖:spring-cloud-starter-alibaba-nacos-config
 * 2、创建一个配置文件:bootstrap.properties(SpringBoot规定的)
 *    2.1 配置当前应用名称
 *    2.2 配置nacos配置中心服务器地址
 * 3、需要给nacos配置中心默认添加名为 “当前应用名.properties” 的数据集(DataId)
 * 4、在上述配置文件中添加配置内容
 * 5、如何动态获取配置？
 *    5.1 添加注解: @RefreshScope, 以此来动态刷新配置
 *    5.2 使用 @Value("${xxx}") 获取某个配置的值
 * 6、如果配置中心和当前应用的配置文件都配置了相同的项, 优先使用配置中心的配置
 *
 * @author wangxl
 * @since 2022/4/29 20:17
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCouponApplication.class, args);
    }
}
