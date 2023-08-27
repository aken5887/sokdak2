package com.project.dailylog.api.domain;

import com.project.dailylog.api.request.PostCreate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name="tb_post")
@NoArgsConstructor
public class Post extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String userId;
  @Lob
  private String content;
  private int count=0;

  @Builder
  public Post(String title, String userId, String content){
    this.title = title;
    this.userId = userId;
    this.content = content;
  }

  public Post toEntity(PostCreate postCreate){
    this.title = postCreate.getTitle();
    this.userId = postCreate.getWriter();
    this.content = postCreate.getContent();
    return this;
  }
}
