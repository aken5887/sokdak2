package com.project.dailylog.api.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends CommonException{

  private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

  public InvalidPasswordException(){
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }
}
