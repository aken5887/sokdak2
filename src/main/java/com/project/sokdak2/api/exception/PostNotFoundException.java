package com.project.sokdak2.api.exception;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends CommonException {

  private static final String MESSAGE = "존재하지 않는 게시글입니다.";

  public PostNotFoundException() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.NOT_FOUND.value();
  }
}
