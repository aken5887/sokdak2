package com.project.dailylog.api.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class InvalidRequestException extends CommonException{

  private static final String MESSAGE = "잘못된 요청입니다.";

  public InvalidRequestException(BindingResult bindingResult) {
    super(MESSAGE);
    bindingResult.getFieldErrors().stream()
        .forEach(error -> {
          addValidation(error.getField(), error.getDefaultMessage());
        });
  }

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
