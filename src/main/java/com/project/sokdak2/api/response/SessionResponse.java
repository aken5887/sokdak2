package com.project.sokdak2.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionResponse {

  private final String accessToken;

  @Builder
  public SessionResponse(String accessToken) {
    this.accessToken = accessToken;
  }

}
