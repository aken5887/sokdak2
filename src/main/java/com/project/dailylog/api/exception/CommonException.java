package com.project.dailylog.api.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class CommonException extends RuntimeException{

  private final Map<String, Object> validation = new HashMap<>();
  private String field;
  private String fieldMessage;

  public CommonException(String message){
    super(message);
  }

  public abstract int getStatusCode();

  public void addValidation(String field, String fieldMessage) {
    this.field = field;
    this.fieldMessage = fieldMessage;
    this.validation.put(field, fieldMessage);
  }
}
