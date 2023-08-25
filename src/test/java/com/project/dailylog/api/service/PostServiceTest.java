package com.project.dailylog.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.PostRepository;
import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.response.PostResponse;
import java.util.ArrayList;
import java.util.List;
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
  }

  @Test
  @DisplayName("없는 게시글을 조회할 때 Exception 발생시킨다.")
  void get_exception() {
    // expected
    assertThatThrownBy(
        () -> postService.get(2L)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("여러 건의 데이터를 조회한다.")
  void list() {
    // given
    List<Post> posts = new ArrayList<>();
    for(int i=0; i<10; i++){
      posts.add(Post.builder()
              .title("제목 :"+i)
              .content("내용 :"+i)
              .build());
    }
    postRepository.saveAll(posts);

    // when
    List<PostResponse> postResponses = postService.getList();

    // then
    assertThat(postResponses.size()).isEqualTo(posts.size());
  }

}