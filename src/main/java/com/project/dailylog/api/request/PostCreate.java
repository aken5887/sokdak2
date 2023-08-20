package com.project.dailylog.api.request;

import javax.validation.constraints.NotBlank;
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
  private String writer;
  private String content;
}
