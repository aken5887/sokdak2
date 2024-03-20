package com.project.sokdak2.api.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author         : choi
 * date           : 2024-03-04
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EhcacheController {
    @Resource(name="ehCacheManager")
    private CacheManager cacheManager;

    @GetMapping("/ehcache")
    public Object findAll() {
        List<Map<String, List<String>>> result
                = cacheManager.getCacheNames().stream()
                .map(cacheName -> {
                    JCacheCache cache = (JCacheCache) cacheManager.getCache(cacheName);
                    Cache<Object, Object> jCache = cache.getNativeCache();
                    Map<String, List<String>> entry = new HashMap<>();
                    for(Cache.Entry<Object, Object> etr: jCache){
                        Object value = etr.getValue();
                        if(value != null){
                            String str = value.toString();
                            entry.computeIfAbsent(cacheName, k-> new ArrayList<>())
                                    .add(str);
                        }
                    }
                    return entry;
                }).collect(Collectors.toList());

        return result;
    }
}
