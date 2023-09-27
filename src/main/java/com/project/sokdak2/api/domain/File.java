package com.project.sokdak2.api.domain;

import com.project.sokdak2.api.util.FileUtil;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name="tb_file")
@NoArgsConstructor
public class File extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String uploadPath;

  private String originalFileName;

  private String realFileName;

  private String fileSize;

  private String fileExt;

  // 관계의 주인
  @ManyToOne
  @JoinColumn(name="post_id")
  private Post post;

  public void setPost(Post post){
    // 기존 관계 제거
    if(this.post != null){
      this.post.getFiles().remove(this);
    }
    this.post = post;
    this.post.getFiles().add(this);
  }

  @Builder
  public File(String uploadPath, String originalFileName, String realFileName, long fileSize, Post post) {
    this.uploadPath = uploadPath;
    this.originalFileName = originalFileName;
    this.realFileName = realFileName;
    this.fileSize = FileUtil.byteToKbytes(fileSize);
    this.fileExt = FileUtil.getFileExt(originalFileName);
    this.post = post;
    post.getFiles().add(this);
  }
}
