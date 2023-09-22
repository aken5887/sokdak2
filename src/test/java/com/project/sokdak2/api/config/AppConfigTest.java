package com.project.sokdak2.api.config;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class AppConfigTest {

  @Autowired
  AppConfig appConfig;

  @Autowired
  ApplicationContext applicationContext;

  @DisplayName("객체에 바인딩된 application.yml 프로퍼티 값을 읽어온다")
  @Test
  void appConfig() {
  }

  @DisplayName("Spring Container에 등록된 bean을 출력한다.")
  @Test
  void beans() {
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    System.out.println("beans size -> "+beanNames.length);
    Arrays.stream(beanNames)
        .forEach(beanName -> {
          Object bean = applicationContext.getBean(beanName);
          System.out.println(beanName + ":" + bean.getClass().getName());
          System.out.println();
        });
  }
}