package com.project.sokdak2.api.response;

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

  private final String code;
  private final String message;
  private final Map<String, Object> validation;

  @Builder
  public ErrorResponse(String code, String message, Map<String, Object> validation) {
    this.code = code;
    this.message = message;
    this.validation = validation;
  }

  public void addValidation(String field, String defaultMessage){
    this.validation.put(field, defaultMessage);
  }
}
