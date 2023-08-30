package com.project.dailylog.api.domain;

import com.project.dailylog.api.request.PostCreate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name="tb_post")
@NoArgsConstructor
public class Post extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String userId;
  @Lob
  private String content;
  @ColumnDefault("0")
  private int count;

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

  public PostEditor.PostEditorBuilder toEditor(){
    PostEditor.PostEditorBuilder editorBuilder = PostEditor.builder()
        .title(this.title)
        .content(this.content);
    return editorBuilder;
  }

  public Post edit(PostEditor postEditor) {
    this.title = postEditor.getTitle();
    this.content = postEditor.getContent();
    return this;
  }
}
