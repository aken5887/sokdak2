package com.project.sokdak2;

import com.project.sokdak2.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
// 설정 클래스가 활성화되면, 해당 클래스가 스프링 빈으로 자동 등록됩니다.
@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication(
        exclude = {
                org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
                org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
                org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
        }
)
public class Sokdak2Application {
  public static void main(String[] args) {
    SpringApplication.run(Sokdak2Application.class, args);
  }
}
