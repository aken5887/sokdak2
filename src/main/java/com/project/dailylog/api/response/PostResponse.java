package com.project.dailylog.api.response;

import com.project.dailylog.api.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

  private Long id;
  private String title;
  private String userId;
  private String content;
  private LocalDateTime createdTime;
  private LocalDateTime lastUpdatedTime;

  public PostResponse(Post post) {
    this.id = id;
    this.title = post.getTitle();
    this.userId = post.getUserId();
    this.content = post.getContent();
    this.createdTime = post.getCreatedTime();
    this.lastUpdatedTime = post.getLastUpdatedTime();
  }

  @Builder
  public PostResponse(Long id, String title, String userId, String content) {
    this.id = id;
    this.title = title.substring(0, Math.min(20, title.length())); // 제목은 20자 제한
    this.userId = userId;
    this.content = content;
  }
}
