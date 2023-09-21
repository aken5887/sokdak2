package com.project.dailylog.api.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class RedisRepositoryConfigTest {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Value("${me.cache}")
  private String cacheStore;

  @DisplayName("기본 Redis 접속 테스트")
  @Test
  void redis_connection_test() {
    // given
    final String key = "myKey";
    final String data = "myData";
    System.out.println("cache store : "+cacheStore);
    if("redis".equals(cacheStore)){
      // when
      final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
      valueOperations.set(key, data);
      // then
      final String s = valueOperations.get(key);
      assertThat(s).isEqualTo(data);
    }
  }
}