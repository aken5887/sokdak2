package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.domain.post.PostReply;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostReplyRepositoryTest {

  @Autowired
  PostReplyRepository postReplyRepository;

  @Autowired
  PostRepository postRepository;
  @MockBean
  SimpleMessageListenerContainer simpleMessageListenerContainer;
  @AfterEach
  public void cleanup(){
    this.postReplyRepository.deleteAll();
    this.postRepository.deleteAll();
  }

  @DisplayName("댓글과 포스트 글을 정상적으로 저장한다.")
  @Transactional
  @Test
  void test1() {
    // given
    Post post = Post.builder()
        .title("1234")
        .password(1234)
        .content("1234")
        .userId("abcd")
        .build();
    postRepository.save(post);

    List<PostReply> replyList  = IntStream.range(0, 10)
        .mapToObj(i -> PostReply.builder()
            .userId("abcd"+i)
            .content("내용-"+i)
            .password(1234)
            .post(post)
            .build())
        .collect(Collectors.toList());

    List<PostReply> savedReplies = (List<PostReply>) postReplyRepository.saveAll(replyList);
    // expected
    assertThat(savedReplies.size()).isEqualTo(10);
    assertThat(savedReplies.get(0).getPost().getTitle()).isEqualTo(post.getTitle());
    assertThat(post.getReplies().size()).isEqualTo(10);
  }
}