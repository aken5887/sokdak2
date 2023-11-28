package com.project.sokdak2.api.request;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Login {
  @NotBlank(message = "email은 필수 값입니다.")
  private final String email;
  @NotBlank(message = "패스워드는 필수 값입니다.")
  private final String password;

  @Builder
  public Login(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
