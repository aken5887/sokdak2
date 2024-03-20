package com.project.sokdak2.api.config;

import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.io.IOException;

/**
 * author         : choi
 * date           : 2024-03-20
 */

@Configuration
public class ECacheConfig {
    @Bean(name = "ehCacheManager")
    public org.springframework.cache.CacheManager ehCacheManager() throws IOException {
        CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        CacheManager manager = cachingProvider.getCacheManager(
                new ClassPathResource("/ehcache.xml").getURI(),
                getClass().getClassLoader());
        return new JCacheCacheManager(manager);
    }
}
