package com.sevensegment.jobis.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache jobPostingsCache = buildCache("jobPostings", 300, 1000);
        CaffeineCache searchResultsCache = buildCache("searchResults", 300, 100);

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(jobPostingsCache, searchResultsCache));
        return cacheManager;
    }

    private CaffeineCache buildCache(String name, int secondsToExpire, int maxSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(secondsToExpire, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .build());
    }
}