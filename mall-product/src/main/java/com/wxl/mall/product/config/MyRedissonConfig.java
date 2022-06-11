package com.wxl.mall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 程序化配置 Redisson
 *
 * @author wangxl
 * @since 2022/6/11 15:22
 */
@Configuration
public class MyRedissonConfig {

    /**
     * 所有对 Redisson 的使用都是通过 RedissonClient
     *
     * @return RedissonClient
     * @throws IOException IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        // 1、创建配置
        Config config = new Config();
        // Redis url should start with redis:// or rediss:// (安全连接)
        config.useSingleServer().setAddress("redis://192.168.56.10:6379");

        // 2、根据 Config 创建出 RedissonClient 实例
        return Redisson.create(config);
    }
}
