package com.project.dailylog.api.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends CommonException{

  private static final String MESSAGE = "잘못된 요청입니다.";

  @Builder
  public InvalidRequestException(String field, String fieldMessage){
    super(MESSAGE);
    addValidation(field, fieldMessage);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }
}
