package com.project.dailylog.api.exception;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends CommonException{

  private static final String MESSAGE = "지정된 파일을 찾을 수 없습니다.";

  public FileNotFoundException(){
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }
}
