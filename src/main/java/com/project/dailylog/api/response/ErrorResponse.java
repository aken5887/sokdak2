package com.project.dailylog.api.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

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
public class ErrorResponse {

  private final int code;
  private final String message;
  private final Map<String, Object> validation = new HashMap<>();

  @Builder
  public ErrorResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public void addValidation(String field, String defaultMessage){
    this.validation.put(field, defaultMessage);
  }
}
