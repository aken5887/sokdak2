package com.project.sokdak2.api.domain.common;

import com.project.sokdak2.api.util.FileUtil;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

  @Builder
  public File(String uploadPath, String originalFileName, String realFileName, long fileSize) {
    this.uploadPath = uploadPath;
    this.originalFileName = originalFileName;
    this.realFileName = realFileName;
    this.fileSize = FileUtil.byteToKbytes(fileSize);
    this.fileExt = FileUtil.getFileExt(originalFileName);
  }
}
