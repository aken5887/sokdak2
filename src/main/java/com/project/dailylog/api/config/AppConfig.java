package com.project.dailylog.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="me")
public class AppConfig {
  private String cache;
  private String jwt;
}
