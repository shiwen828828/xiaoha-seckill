package com.zhuxiaoha.seckill.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author: 猪小哈
 * @Date: 2026-06-10
 * @Version: v1.0.0
 * @Description: 本地缓存配置
 **/
@Configuration
public class LocalCacheConfig {

    /**
     * 商品列表本地缓存
     * <p>
     * 最大缓存 1000 个活动，每个缓存 30 秒后过期
     */
    @Bean
    public Cache<String, String> goodsListLocalCache() {
        return Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(30, TimeUnit.SECONDS).build();
    }


    /**
     * 商品详情本地缓存
     * <p>
     * 最大缓存 5000 个商品，每个缓存 30 秒后过期
     */
    @Bean
    public Cache<String, String> goodsDetailLocalCache() {
        return Caffeine.newBuilder().maximumSize(5000).expireAfterWrite(30, TimeUnit.SECONDS).build();
    }

}
