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
  @ColumnDefault("0000")
  private int password;

  @Builder
  public Post(String title, String userId, String content, int password){
    this.title = title;
    this.userId = userId;
    this.content = content;
    this.password = password;
  }

  public Post toEntity(PostCreate postCreate){
    this.title = postCreate.getTitle();
    this.userId = postCreate.getUserId();
    this.content = postCreate.getContent();
    this.password = postCreate.getPassword()==null? 0000:postCreate.getPassword();
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

  public void increaseCount(){
    this.count++;
  }
}
