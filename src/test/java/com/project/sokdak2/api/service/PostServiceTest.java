package com.project.sokdak2.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.sokdak2.api.domain.Post;
import com.project.sokdak2.api.exception.InvalidPasswordException;
import com.project.sokdak2.api.exception.PageNotFoundException;
import com.project.sokdak2.api.exception.PostNotFoundException;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.request.PostCreate;
import com.project.sokdak2.api.request.PostEdit;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

  @BeforeEach
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
        .password(1234)
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
    Post postCreate = Post.builder()
        .title("테스트 제목2")
        .content("테스트 내용2")
        .build();

    postRepository.save(postCreate);

    // when
    System.out.printf("postCreate id = %d", postCreate.getId());
    PostResponse postResponse = postService.get(postCreate.getId());

    // then
    assertThat(postResponse).isNotNull();
    assertThat(postResponse.getTitle()).isEqualTo(postCreate.getTitle());
    assertThat(postResponse.getContent()).isEqualTo(postCreate.getContent());
    assertThat(postResponse.getCreatedTime()).isNotNull();
    assertThat(postResponse.getLastUpdatedTime()).isNotNull();
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
        .size(10)
        .build();

    // when
    List<PostResponse> postResponses = postService.getList(postSearch);

    // then
    assertThat(postResponses.size()).isEqualTo(postSearch.getSize());
    assertThat(postResponses.get(0).getTitle()).isEqualTo("제목 = 20");
    assertThat(postResponses.get(0).getContent()).isEqualTo("내용 = 20");
  }

  @DisplayName("글 제목과 내용이 정상적으로 수정된다.")
  @Test
  void edit() throws Exception {
    // given
    Post savedPost = postRepository.save(Post
        .builder()
        .title("제목")
        .content("제목")
        .userId("choi")
        .password(1234)
        .build());

    PostEdit postEdit = PostEdit.builder()
        .title("제목(수정)")
        .content("내용(수정)")
        .password(1234)
        .build();

    // when
    postService.edit(savedPost.getId(), postEdit);

    // then
    Post post = postRepository.findById(savedPost.getId())
            .orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
    assertThat(post.getId()).isEqualTo(savedPost.getId());
    assertThat(post.getTitle()).isEqualTo(postEdit.getTitle());
    assertThat(post.getContent()).isEqualTo(postEdit.getContent());
  }

  @DisplayName("글 제목이 null인경우 수정되지 않는다.")
  @Test
  void edit_test2() throws Exception {
    // given
    Post savedPost = postRepository.save(Post
        .builder()
        .title("제목")
        .content("내용")
        .userId("choi")
        .password(1234)
        .build());

    PostEdit postEdit = PostEdit.builder()
        .title(null)
        .content(null)
        .password(null)
        .build();

   // when
    postService.edit(savedPost.getId(), postEdit);

    // then
    Post post = postRepository.findById(savedPost.getId())
        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
    assertThat(post.getId()).isEqualTo(savedPost.getId());
    assertThat(post.getTitle()).isEqualTo("제목");
    assertThat(post.getContent()).isEqualTo("내용");
  }

  @DisplayName("글이 정상적으로 삭제된다.")
  @Test
  void delete_test() {
    // given
    Post post = Post.builder()
        .title("삭제 테스트")
        .content("삭제할 내용")
        .build();

    postRepository.save(post);

    // when
    postService.delete(post.getId());

    // then
    assertThat(postRepository.count()).isEqualTo(0);
  }

  @DisplayName("게시글 조회 - PageNotFoundException")
  @Test
  void get_exception2() {
    // expected
    assertThatThrownBy(() -> postService.get(1L)).isInstanceOf(PageNotFoundException.class)
        .hasMessageContaining("페이지를 찾을 수 없습니다.");
  }

  @DisplayName("게시글 수정 - PostNotFoundException")
  @Test
  void edit_exception() {
    // given
    PostEdit postEdit = PostEdit.builder().build();

    // expected
    PostNotFoundException e
        = Assertions.assertThrows(PostNotFoundException.class, () -> postService.edit(1L, postEdit));
    assertThat(e.getMessage()).isEqualTo("존재하지 않는 게시글입니다.");
  }

  @DisplayName("게시글 삭제 - PostNotFoundException")
  @Test
  void delete_exception() {
    // expected
    assertThatThrownBy(() -> postService.delete(1L)).isInstanceOf(PostNotFoundException.class)
        .hasMessageContaining("존재하지 않는 게시글입니다.");
  }

  @DisplayName("비밀번호가 일치하지 않으면 게시글 수정이 되지 않는다.")
  @Test
  void edit_not_matching_password(){
    // given
    Post post = Post.builder()
        .title("제목")
        .content("내용")
        .password(1234)
        .build();
    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
        .title("수정")
        .content("수정")
        .password(12345)
        .build();

    // expected
    assertThatThrownBy(() -> postService.edit(post.getId(), postEdit))
        .isInstanceOf(InvalidPasswordException.class)
        .hasMessageContaining("비밀번호가 올바르지 않습니다.");
  }
}