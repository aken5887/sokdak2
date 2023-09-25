package com.project.sokdak2.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.sokdak2.api.domain.Post;
import com.project.sokdak2.api.domain.PostReply;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostReplyRepositoryTest {

  @Autowired
  PostReplyRepository postReplyRepository;

  @Autowired
  PostRepository postRepository;

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
    post.addReplies(savedReplies);

    // expected
    assertThat(savedReplies.size()).isEqualTo(10);
    assertThat(savedReplies.get(0).getPost().getTitle()).isEqualTo(post.getTitle());
    assertThat(post.getReplies().size()).isEqualTo(10);
  }
}