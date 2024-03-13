package com.project.sokdak2.api.domain.post;

import com.project.sokdak2.api.domain.common.File;
import com.project.sokdak2.api.request.PostCreate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="tb_post")
@NoArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private Category category;
  private String title;
  private String userId;
  @Lob
  private String content;
  @ColumnDefault("0")
  private int count;
  @ColumnDefault("0000")
  private int password;
  @ColumnDefault("0")
  private Integer locked;
  private LocalDateTime createdTime;
  private LocalDateTime lastUpdatedTime;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<File> files = new ArrayList<>();

  @OneToMany
  private List<PostReply> replies = new ArrayList<>();

  @Builder
  public Post(String title, String userId, String content, int password, Integer locked, Category category){
    this.title = title;
    this.userId = userId;
    this.content = content;
    this.password = password;
    this.locked = locked;
    this.category = category;
  }

  public Post toEntity(PostCreate postCreate){
    this.title = postCreate.getTitle();
    this.userId = postCreate.getUserId();
    this.content = postCreate.getContent();
    this.password = postCreate.getPassword()==null? 0000:postCreate.getPassword();
    this.locked = postCreate.getLocked();
    this.category = Category.BBS;
    this.createdTime = LocalDateTime.now();
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
    this.locked = postEditor.getLocked();
    this.lastUpdatedTime = LocalDateTime.now();
    return this;
  }

  public void increaseCount(){
    this.count++;
  }

  public void addFile(File file){
    this.files.add(file);
  }

  public void addFiles(List<File> files){
    this.files.addAll(files);
  }

  public void addReplies(List<PostReply> replies){
    this.replies.addAll(replies);
    replies.forEach(r -> r.updatePost(this));
  }
}
