package com.project.sokdak2.api.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CommonException{
  private static final String MESSAGE = "권한이 없습니다.";

  public UnAuthorizedException() {
    super(MESSAGE);
  }

  public UnAuthorizedException(String inputMessage) {
    super(inputMessage);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.UNAUTHORIZED.value();
  }
}
