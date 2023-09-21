package com.project.dailylog;

import com.project.dailylog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

// 설정 클래스가 활성화되면, 해당 클래스가 스프링 빈으로 자동 등록됩니다.
@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class  DailylogApplication {
  public static void main(String[] args) {
    SpringApplication.run(DailylogApplication.class, args);
  }
}
