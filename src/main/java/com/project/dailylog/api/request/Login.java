package com.project.dailylog.api.request;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Login {
  @NotBlank(message = "아이디는 필수 값입니다.")
  private final String userId;
  @NotBlank(message = "패스워드는 필수 값입니다.")
  private final String password;

  @Builder
  public Login(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }
}
