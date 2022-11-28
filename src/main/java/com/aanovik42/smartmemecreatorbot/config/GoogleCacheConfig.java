package com.aanovik42.smartmemecreatorbot.config;

import com.aanovik42.smartmemecreatorbot.entity.Chat;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleCacheConfig {

    private final long EXPIRATION_TIME;

    public GoogleCacheConfig(@Value("${google.cache.expiration-time-sec}") long EXPIRATION_TIME) {
        this.EXPIRATION_TIME = EXPIRATION_TIME;
    }

    @Bean
    public Cache<String, Chat> getCache() {

        Cache<String, Chat> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRATION_TIME, TimeUnit.SECONDS)
                .build();

        return cache;
    }
}
