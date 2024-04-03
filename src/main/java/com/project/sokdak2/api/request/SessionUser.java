package com.project.sokdak2.api.request;

import com.project.sokdak2.api.domain.user.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class SessionUser {
  private Long id;
  private String userId;
  private String name;
  private String email;
  private String password;
  private Role role;
}
