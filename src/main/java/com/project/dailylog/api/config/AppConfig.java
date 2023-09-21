package com.project.dailylog.api.config;

import java.util.Base64;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="me")
public class AppConfig {

  private byte[] jwtKey;

  public byte[] getJwtKey() {
    return jwtKey;
  }

  public void setJwtKey(String jwtKey) {
    this.jwtKey = Base64.getDecoder().decode(jwtKey);
  }
}
