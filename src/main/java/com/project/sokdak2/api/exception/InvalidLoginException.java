package com.project.sokdak2.api.exception;

import org.springframework.http.HttpStatus;

public class InvalidLoginException extends CommonException{

  private static String MESSAGE = "로그인 정보가 올바르지 않습니다.";

  public InvalidLoginException(){
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }
}
