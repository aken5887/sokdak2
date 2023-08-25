package com.project.dailylog.api.response;

import com.project.dailylog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

  private Long id;
  private String title;
  private String userId;
  private String content;

  public PostResponse(Post post) {
    this.id = id;
    this.title = post.getTitle();
    this.userId = post.getUserId();
    this.content = post.getContent();
  }

  @Builder
  public PostResponse(Long id, String title, String userId, String content) {
    this.id = id;
    this.title = title.substring(0, Math.min(20, title.length())); // 제목은 20자 제한
    this.userId = userId;
    this.content = content;
  }
}
