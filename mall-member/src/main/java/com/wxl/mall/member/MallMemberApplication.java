package com.wxl.mall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * (测试OpenFeign调用)想要远程调用别的服务
 * 1.引入OpenFeign
 * 2.编写一个接口(CouponFeignService), 告诉SpringCloud这个接口需要调用远程服务
 * 写到feign包下, 这样以后一看就知道是远程接口
 *   2.1 声明接口的每一个方法都是调用哪个远程服务的哪个请求
 * 3.开启远程调用功能:@EnableFeignClients, 服务启动后会自动扫描feign包下所有标了FeignClient注解的接口
 *
 * @author wangxl
 * @since 2022/4/29 20:17
 */
@EnableFeignClients(basePackages = "com.wxl.mall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MallMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallMemberApplication.class, args);
    }
}
