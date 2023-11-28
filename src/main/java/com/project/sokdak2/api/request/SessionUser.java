package com.project.sokdak2.api.request;

import com.project.sokdak2.api.domain.Session;
import com.project.sokdak2.api.domain.User;
import lombok.*;

@ToString
@Builder
@Getter
public class SessionUser {
  private Long id;
  private String name;
  private String email;
}
