package com.project.dailylog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class SessionUser {
  private String userId;
  private String userName;
}
