package com.wxl.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 整合 MyBatis-Plus
 *   1)导入依赖:mybatis-plus-boot-starter
 *   2)配置
 *      2.1 配置数据源
 *          1、pom.xml导入数据库驱动(common-module)
 *          2、在application.yml配置数据源相关信息
 *      2.2 配置MyBatis-Plus
 *          1、使用 @MapperScan 指定 Mapper类所在的包
 *          2、告诉MyBatis-Plus, SQL映射文件位置(在yml配置文件中配置)
 *
 * @author wangxl
 * @since 2022/4/29 20:19
 */
@MapperScan(value = "com.wxl.mall.product.dao")
@SpringBootApplication
public class MallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }
}
