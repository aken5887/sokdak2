package com.project.dailylog.api.controller;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.repository.PostRepository;
import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostEdit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  PostRepository postRepository;

  @BeforeEach
  void setup() {
    postRepository.deleteAll();
  }

  @Test
  @DisplayName("/posts를 JSON객체로 POST요청하면 Status 200을 리턴한다.")
  void create() throws Exception {
    // given
    PostCreate postCreate = PostCreate.builder()
        .title("제목입니다.")
        .writer("작성자입니다.")
        .content("내용입니다.")
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);

    // expected
    this.mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postCreateStr))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("/posts POST 요청시 title과 writer는 필수 값이다.")
  void create2() throws Exception {
    // given
    PostCreate postCreate = PostCreate.builder()
        .title(null)
        .writer(null)
        .content("내용입니다.")
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);

    // expected
    this.mockMvc.perform(post("/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(postCreateStr))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.writer").value("작성자는 필수입니다."));
  }

  @Test
  @DisplayName("/posts를 JSON객체로 POST요청하면 정상적으로 저장된다.")
  void create3() throws Exception {
    //given
    PostCreate postCreate = PostCreate.builder()
        .title("제목입니다.")
        .writer("작성자입니다.")
        .content("내용입니다.")
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);
    // when
    this.mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postCreateStr))
        .andExpect(status().isOk());
    // then
    Assertions.assertThat(postRepository.count()).isEqualTo(1);
    Assertions.assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo("제목입니다.");
  }

  @Test
  @DisplayName("/posts 단건 데이터를 조회한다.")
  void get1() throws Exception {
    // given
    Post post = Post.builder()
        .title("제목1")
        .content("내용1")
        .userId("choi")
        .build();
    postRepository.save(post);

    // when
    this.mockMvc.perform(get("/posts/{postId}", post.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value(post.getTitle()))
        .andExpect(jsonPath("$.content").value(post.getContent()));
  }

  @Test
  @DisplayName("/posts 요청으로 2페이지 데이터 여러건을 조회한다.")
  void list() throws Exception {
    // given
    postRepository.saveAll(IntStream.range(0,31)
        .mapToObj(i -> Post.builder()
              .title("제목-"+i)
              .content("내용-"+i)
              .userId("choi-"+i)
              .build()).collect(Collectors.toList())
    );

    // expected
    this.mockMvc.perform(get("/posts?page=2&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(10)))
        .andExpect(jsonPath("$[0].title").value("제목-20"))
        .andExpect(jsonPath("$[0].content").value("내용-20"));
  }

  @Test
  @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다")
  void list1() throws Exception{
    // given
    postRepository.saveAll(
        IntStream.range(0,21)
        .mapToObj(i -> Post.builder()
            .title("제목"+i)
            .content("내용"+i)
            .userId("user"+i)
            .build())
        .collect(Collectors.toList()));

    // expected
    this.mockMvc.perform(get("/posts?page=0&size=20")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value("20"))
        .andExpect(jsonPath("$[0].title").value("제목20"));
  }

  @DisplayName("글 제목 및 내용 수정")
  @Test
  void edit() throws Exception {
    // given
    Post post = Post.builder()
        .title("제목")
        .content("내용")
        .userId("writer")
        .build();
    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
        .title("제목(수정)")
        .content("내용(수정)")
        .build();

    String value = objectMapper.writeValueAsString(postEdit);

    // expected
    this.mockMvc.perform(patch("/posts/{postId}", post.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(value))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("제목(수정)"))
        .andExpect(jsonPath("$.content").value("내용(수정)"));
  }
}