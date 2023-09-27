package com.project.sokdak2.api.request;

import com.project.sokdak2.api.exception.InvalidRequestException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
  @NotNull(message="암호는 필수입니다.")
  private final Integer password;

  public void validate(){
    if(this.title.contains("테스트")){
      throw InvalidRequestException.builder()
          .field("title")
          .fieldMessage("제목엔 '테스트'가 들어갈 수 없습니다.")
          .build();
    }
  }
}