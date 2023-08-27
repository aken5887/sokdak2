package com.project.dailylog.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.PostRepository;
import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.response.PostResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

  @Autowired
  PostService postService;

  @Autowired
  PostRepository postRepository;

  @AfterEach
  void clean() {
    postRepository.deleteAll();
  }

  @Test
  @DisplayName("POST 엔티티를 저장한다.")
  void save() {
    // given
    PostCreate postCreate = PostCreate.builder()
        .title("테스트 제목")
        .content("테스트 내용")
        .build();

    // when
    postService.save(postCreate);

    // then
    assertThat(postRepository.count()).isEqualTo(1L);
  }

  @Test
  @DisplayName("저장된 POST 엔티티를 조회한다.")
  void get() {
    // given
    PostCreate postCreate = PostCreate.builder()
        .title("테스트 제목2")
        .content("테스트 내용2")
        .build();

    postService.save(postCreate);

    // when
    PostResponse postResponse = postService.get(1L);

    // then
    assertThat(postResponse).isNotNull();
    assertThat(postResponse.getTitle()).isEqualTo(postCreate.getTitle());
    assertThat(postResponse.getContent()).isEqualTo(postCreate.getContent());
    assertThat(postResponse.getCreatedTime()).isBefore(LocalDateTime.now());
    assertThat(postResponse.getLastUpdatedTime()).isBefore(LocalDateTime.now());
  }

  @Test
  @DisplayName("없는 게시글을 조회할 때 Exception 발생시킨다.")
  void get_exception() {
    // expected
    assertThatThrownBy(
        () -> postService.get(2L)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("여러 건의 데이터 중 2페이지를 조회한다.")
  void list() {
    // given
    List<Post> posts = IntStream.range(0, 31)
            .mapToObj(
                i -> Post.builder()
                    .title("제목 = "+i)
                    .content("내용 = "+i)
                    .userId("작성자"+i)
                    .build()
            ).collect(Collectors.toList());
    postRepository.saveAll(posts);

    PostSearch postSearch = PostSearch.builder()
        .page(2)
        .build();

    // when
    List<PostResponse> postResponses = postService.getList(postSearch);

    // then
    assertThat(postResponses.size()).isEqualTo(postSearch.getSize());
    assertThat(postResponses.get(0).getTitle()).isEqualTo("제목 = 20");
    assertThat(postResponses.get(0).getContent()).isEqualTo("내용 = 20");
  }

}