package com.wxl.mall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存相关的配置
 *
 * @author wangxl
 * @since 2022/6/12 18:44
 */
@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class MyCacheConfig {

    // 方式一
//    @Resource
//    private CacheProperties cacheProperties;

    /**
     * 自定义时, 没有读取配置文件内容, 这里需要补一下
     * 1、原来和配置文件绑定的配置类为：@ConfigurationProperties(prefix = "spring.cache")
     * 2、要让他生效，要加上 @EnableConfigurationProperties(CacheProperties.class)
     *    方式二:入参
     *
     * @return RedisCacheConfiguration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();

//        defaultCacheConfig = defaultCacheConfig.entryTtl();
        defaultCacheConfig = defaultCacheConfig.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer()
        ));
        defaultCacheConfig = defaultCacheConfig.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()
        ));

        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        //将配置文件中所有的配置都生效
        if (redisProperties.getTimeToLive() != null) {
            defaultCacheConfig = defaultCacheConfig.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            defaultCacheConfig = defaultCacheConfig.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            defaultCacheConfig = defaultCacheConfig.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            defaultCacheConfig = defaultCacheConfig.disableKeyPrefix();
        }

        return defaultCacheConfig;
    }
}
