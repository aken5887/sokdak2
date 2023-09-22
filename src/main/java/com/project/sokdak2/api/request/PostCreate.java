package com.project.sokdak2.api.request;

import com.project.sokdak2.api.exception.InvalidRequestException;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

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
  private Integer topFixed;
  private List<MultipartFile> files;

  public void validate() {
    if(this.title != null && this.title.contains("테스트")){
        throw InvalidRequestException.builder()
              .field("title")
              .fieldMessage("제목엔 '테스트'가 포함될 수 없습니다.")
              .build();
    }
  }
}
