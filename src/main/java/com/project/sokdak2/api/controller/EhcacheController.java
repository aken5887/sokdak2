package com.project.sokdak2.api.controller;

import lombok.RequiredArgsConstructor;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final CacheManager cacheManager;

    @GetMapping("/ehcache")
    public Object findAll() {
        List<Map<String, List<String>>> result
                = cacheManager.getCacheNames().stream()
                .map(cacheName -> {
                    EhCacheCache cache = (EhCacheCache) cacheManager.getCache(cacheName);
                    Ehcache ehcache = cache.getNativeCache();
                    Map<String, List<String>> entry = new HashMap<>();

                    ehcache.getKeys().forEach(key -> {
                        Element element = ehcache.get(key);
                        String str = "";
                        if(element != null){
//                            if(element.getObjectValue() instanceof PageImpl){
//                                Page<PostResponse> p = (Page<PostResponse>) element.getObjectValue();
//                                StringBuilder sb = new StringBuilder();
//                                sb.append("cacheName : ").append(cacheName).append(",");
//                                sb.append("key : ").append(element.getObjectKey()).append(" -> ");
//                                for(PostResponse post:p.getContent()){
//                                    sb.append(post.toString()).append(",");
//                                }
//                                str = sb.toString();
//                            } else {
                               str = element.toString();
//                            }
                            entry.computeIfAbsent(cacheName, k-> new ArrayList<>())
                                    .add(str);
                        }
                    });
                    return entry;
                }).collect(Collectors.toList());

        return result;
    }
}
