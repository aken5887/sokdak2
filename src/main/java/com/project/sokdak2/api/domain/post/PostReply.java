package com.project.sokdak2.api.domain.post;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.project.sokdak2.api.domain.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity(name="tb_reply")
@NoArgsConstructor
public class PostReply extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Lob
  private String content;

  private String userId;

  @ColumnDefault("0000")
  private Integer password;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @Builder
  public PostReply(String content, String userId, Integer password, Post post) {
    this.content = content;
    this.userId = userId;
    this.password = password;
    this.post = post;
    post.getReplies().add(this);
  }

  public void updatePost(Post post) {
    this.post = post;
  }

}
