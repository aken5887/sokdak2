package com.project.dailylog.api.response;

import com.project.dailylog.api.domain.File;
import com.project.dailylog.api.domain.Post;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  }

  @Builder
  public PostResponse(Long id, String title, String userId, String content) {
    this.id = id;
    this.title = title.substring(0, Math.min(20, title.length())); // 제목은 20자 제한
    this.userId = userId;
    this.content = content;
  }
}
