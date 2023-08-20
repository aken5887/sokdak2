package com.project.dailylog.api;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *  {
 *    code : "400"
 *    message : "잘못된 요청입니다.",
 *    validation : {
 *      "title": "제목은 필수 값입니다."
 *    }
 *  }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {

  private final String code;
  private final String message;
  private final Map<String, Object> validation = new HashMap<>();

  public void addValidation(String field, String defaultMessage){
    this.validation.put(field, defaultMessage);
  }
}
