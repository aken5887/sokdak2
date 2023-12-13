package com.project.sokdak2.api.exception;

import org.aspectj.weaver.GeneratedReferenceTypeDelegate;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends GeneralException {

  private static final String MESSAGE = "지정된 파일을 찾을 수 없습니다.";

  public FileNotFoundException(){
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return HttpStatus.NOT_FOUND.value();
  }
}
