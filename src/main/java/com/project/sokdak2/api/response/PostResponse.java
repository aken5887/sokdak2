package com.project.sokdak2.api.response;

import com.project.sokdak2.api.domain.common.File;
import com.project.sokdak2.api.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse {

  private Long id;
  private String title;
  private String userId;
  private String content;
  private int count;
  private String createdTime;
  private String lastUpdatedTime;
  private List<File> files;
  private Integer locked;
  private Integer password;

  public PostResponse(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.userId = post.getUserId();
    this.content = post.getContent();
    this.count = post.getCount();
    this.createdTime
        = post.getCreatedTime() != null ?
          post.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
          "";
    this.lastUpdatedTime
        = post.getLastUpdatedTime() != null ?
          post.getLastUpdatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
          "";
    this.files = post.getFiles();
    this.locked = post.getLocked();
    this.password = post.getPassword();
  }

  @Builder
  public PostResponse(Long id, String title, String userId, String content, Integer locked) {
    this.id = id;
    this.title = title.substring(0, Math.min(20, title.length())); // 제목은 20자 제한
    this.userId = userId;
    this.content = content;
    this.locked = locked;
  }
}
