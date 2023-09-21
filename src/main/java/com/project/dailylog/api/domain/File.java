package com.project.dailylog.api.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

  private long fileSize;

  @ManyToOne
  private Post post;

  @Builder
  public File(String uploadPath, String originalFileName, String realFileName, long fileSize, Post post) {
    this.uploadPath = uploadPath;
    this.originalFileName = originalFileName;
    this.realFileName = realFileName;
    this.fileSize = fileSize;
    this.post = post;
  }

}
