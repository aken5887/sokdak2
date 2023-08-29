package com.project.dailylog.api.request;

import com.project.dailylog.api.exception.InvalidRequestException;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostEdit {
  @NotBlank(message="제목은 필수입니다.")
  private final String title;
  @NotBlank(message="내용은 필수입니다.")
  private final String content;
  private final String userId;

  public void validate(){
    if(this.title.contains("테스트")){
      throw InvalidRequestException.builder()
          .field("title")
          .fieldMessage("제목엔 '테스트'가 들어갈 수 없습니다.")
          .build();
    }
  }
}
