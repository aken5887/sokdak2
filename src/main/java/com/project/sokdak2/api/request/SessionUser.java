package com.project.sokdak2.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class SessionUser {
  private Long id;
  private String name;
  private String email;
}
