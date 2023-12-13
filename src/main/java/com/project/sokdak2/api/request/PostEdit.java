package com.project.sokdak2.api.request;

import com.project.sokdak2.api.exception.InvalidRequestException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
  private List<MultipartFile> files;
  private String delChk1;
  private String delChk2;
  private Long delFile1;
  private Long delFile2;

  public void validate(){
    if(this.title.contains("테스트")){
      throw InvalidRequestException.builder()
          .field("title")
          .fieldMessage("제목엔 '테스트'가 들어갈 수 없습니다.")
          .build();
    }
  }

  public boolean isFile1Edit(PostEdit postEdit){
    if(postEdit.getDelChk1() != null & postEdit.getFiles() != null){
        return "1".equals(postEdit.getDelChk1()) || !postEdit.getFiles().get(0).isEmpty();
    }
    return false;
  }

  public boolean isFile2Edit(PostEdit postEdit){
    if(postEdit.getDelChk2() != null & postEdit.getFiles() != null){
        return "1".equals(postEdit.getDelChk2()) || !postEdit.getFiles().get(1).isEmpty();
    }
    return false;
  }
}
