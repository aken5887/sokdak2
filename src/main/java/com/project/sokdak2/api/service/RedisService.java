package com.project.sokdak2.api.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

  private final Long expiredDurationSec = 86400L;
  private final RedisTemplate<String, Boolean> redisTemplate;

  public boolean isFirstIpRequest(String clientAddress, Long postId){
    String key = generateKey(clientAddress, postId);
    log.info("user key : {}", key);
    if(redisTemplate.hasKey(key)){
      return false;
    }
    return true;
  }

  public void writeClientRequest(String clientAddress, Long postId){
    String key = generateKey(clientAddress, postId);
    log.info("user key : {}", key);

    redisTemplate.opsForValue().set(key, true);
    redisTemplate.expire(key, expiredDurationSec, TimeUnit.SECONDS);
  }

  private String generateKey(String clientAddress, Long postId){
    return clientAddress + "+" + postId;
  }
}
