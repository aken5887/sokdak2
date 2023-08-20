package com.project.dailylog.api.controller;

import com.project.dailylog.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse invalidRequestErrorHandler(MethodArgumentNotValidException e){
    log.error("invalidRequestErrorHandler : {} ", e);
    ErrorResponse errorResponse = new ErrorResponse("400", "잘못된 요청입니다.");
    for(FieldError fieldError:e.getFieldErrors()){
      errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return errorResponse;
  }

}
