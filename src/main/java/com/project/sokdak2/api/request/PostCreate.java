package com.project.sokdak2.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
  private Integer locked;
  private List<MultipartFile> files;
  private Long postUserId;
  private String createdTime;

  public void validate() {
  }
}
