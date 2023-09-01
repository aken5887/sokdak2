package com.project.dailylog.api.request;

import com.project.dailylog.api.exception.InvalidRequestException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class PostCreate {

  @NotBlank(message = "제목은 필수입니다.")
  private String title;
  @NotBlank(message = "작성자는 필수입니다.")
  private String userId;
  private String content;
  @NotNull(message="암호는 필수입니다.")
  private Integer password;

  public void validate() {
    if(this.title.contains("테스트")){
        throw InvalidRequestException.builder()
              .field("title")
              .fieldMessage("제목엔 '테스트'가 포함될 수 없습니다.")
              .build();
    }
  }
}
