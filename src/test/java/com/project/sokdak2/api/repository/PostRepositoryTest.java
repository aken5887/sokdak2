package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.response.PostResponse;
import com.project.sokdak2.api.util.PageMaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class PostRepositoryTest {

  @Autowired
  PostRepository postRepository;
  @MockBean
  SimpleMessageListenerContainer simpleMessageListenerContainer;
  @DisplayName("Pageable 객체를 이용해서 조회한다.")
  @Test
  void test() {
    // given
    postRepository.saveAll(
        IntStream.range(0, 31)
            .mapToObj(i -> Post.builder()
                .title("제목")
                .content("내용")
                .userId("writer")
                .build())
            .collect(Collectors.toList())
    );

    PostSearch postSearch = new PostSearch();

    // when
    Page<PostResponse> result = postRepository.findPostsByCondition(postSearch);
    PageMaker<PostResponse> returnResult = new PageMaker<>(result);
    returnResult.getResult().getContent().stream()
        .forEach(post -> {
          System.out.println(post.getTitle());
        });
  }
}