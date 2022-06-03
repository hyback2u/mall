package com.wxl.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 1、整合 MyBatis-Plus
 *   1)导入依赖:mybatis-plus-boot-starter
 *   2)配置
 *      2.1 配置数据源
 *          1、pom.xml导入数据库驱动(common-module)
 *          2、在application.yml配置数据源相关信息
 *      2.2 配置MyBatis-Plus
 *          1、使用 @MapperScan 指定 Mapper类所在的包
 *          2、告诉MyBatis-Plus, SQL映射文件位置(在yml配置文件中配置)
 *
 * 2、使用逻辑删除
 *   1)配置全局的逻辑删除规则
 *   2)配置逻辑删除的组件[注入容器](version < 3.1)
 *   3)实体类字段加上逻辑删除注解: @TableLogic
 *
 * @author wangxl
 * @since 2022/4/29 20:19
 */
@EnableFeignClients(basePackages = "com.wxl.mall.product.feign")
@EnableDiscoveryClient
@MapperScan(value = "com.wxl.mall.product.dao")
@SpringBootApplication
public class MallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }
}
