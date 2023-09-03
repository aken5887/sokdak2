package com.project.dailylog.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.exception.PostNotFoundException;
import com.project.dailylog.api.repository.PostRepository;
import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostEdit;
import com.project.dailylog.api.response.PostResponse;
import com.project.dailylog.api.util.PageMaker;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

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
        .userId("작성자입니다.")
        .content("내용입니다.")
        .password(1234)
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);

    // expected
    this.mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postCreateStr))
        .andDo(print())
        .andExpect(status().isOk());
//        .andExpect(status().is3xxRedirection())
//        .andExpect(view().name(StringContains.containsString("/posts/")))
//        .andExpect(flash().attributeExists("response"));
  }

  @Test
  @DisplayName("/posts POST 요청시 title과 userId는 필수 값이다.")
  void create2() throws Exception {
    // given
    PostCreate postCreate = PostCreate.builder()
        .title(null)
        .userId(null)
        .content("내용입니다.")
        .password(1234)
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);

    // expected
    this.mockMvc.perform(post("/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(postCreateStr))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value(StringContains.containsString("잘못된 요청")))
        .andExpect(jsonPath("$.validation.userId").value("작성자는 필수입니다."));
  }

  @Test
  @DisplayName("/posts를 JSON객체로 POST요청하면 정상적으로 저장된다.")
  void create3() throws Exception {
    //given
    PostCreate postCreate = PostCreate.builder()
        .title("제목입니다.")
        .userId("작성자입니다.")
        .content("내용입니다.")
        .password(1234)
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);
    // when
    this.mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postCreateStr))
        .andDo(print())
        .andExpect(status().isOk());
//        .andExpect(view().name(StringContains.containsString("/posts/")));
    // then
    assertThat(postRepository.count()).isEqualTo(1);
    assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo("제목입니다.");
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
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model()
            .attribute("response", hasProperty("title", is(post.getTitle()))))
        .andExpect(model()
            .attribute("response", hasProperty("content", is(post.getContent()))));
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
    MvcResult mvcResult =
      this.mockMvc.perform(get("/posts?page=2")
          .contentType(MediaType.APPLICATION_JSON))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(model().attribute("response", hasProperty("result")))
          .andExpect(model().attribute("posts", hasSize(10)))
          .andReturn();

    ModelAndView mav = mvcResult.getModelAndView();
    List<PostResponse> posts = (List<PostResponse>) mav.getModel().get("posts");
    assertThat(posts.get(0).getTitle()).isEqualTo("제목-20");
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
        .andExpect(status().isOk());
  }

  @DisplayName("글 제목 및 내용 수정")
  @Test
  void edit() throws Exception {
    // given
    Post post = Post.builder()
        .title("제목")
        .content("내용")
        .userId("writer")
        .password(1234)
        .build();
    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
        .title("제목(수정)")
        .content("내용(수정)")
        .password(1234)
        .build();

    String value = objectMapper.writeValueAsString(postEdit);

    // expected
    this.mockMvc.perform(patch("/posts/{postId}", post.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(value))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("제목(수정)"))
        .andExpect(jsonPath("$.content").value("내용(수정)"));
//        .andExpect(status().is3xxRedirection())
//        .andExpect(redirectedUrl("/posts/"+post.getId()))
//        .andExpect(flash()
//            .attribute("postResponse", hasProperty("title", is("제목(수정)"))))
//        .andExpect(flash()
//            .attribute("postResponse", hasProperty("content", is("내용(수정)"))));
  }

  @DisplayName("/posts DELETE 요청시 게시글이 성공적으로 삭제된다.")
  @Test
  void delete_test() throws Exception {
    // given
    Post post = Post.builder()
        .title("제목")
        .content("내용")
        .build();
    postRepository.save(post);

    // when
    this.mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId()))
            .andDo(print())
            .andExpect(status().isOk());
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/posts"));

    // then
    assertThat(postRepository.count()).isEqualTo(0);
  }

  @DisplayName("존재하지 않는 게시글을 조회하면 404오류가 발생한다.")
  @Test
  void get_not_found() throws Exception {
    // expected
    this.mockMvc.perform(get("/posts/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @DisplayName("존재하지 않는 게시글을 수정하면 404오류가 발생한다.")
  @Test
  void edit_not_found() throws Exception {
    // given
    PostEdit postEdit = PostEdit.builder()
        .title("제목")
        .content("내용")
        .password(1234)
        .build();

    String content = objectMapper.writeValueAsString(postEdit);

    // expected
    this.mockMvc.perform(patch("/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isNotFound());
  }

  @DisplayName("게시글 작성시 제목에 '테스트'가 들어가면 400오류가 발생한다.")
  @Test
  void create_invalid_request() throws Exception {
   // given
   PostCreate postCreate = PostCreate.builder()
       .title("테스트 제목")
       .content("테스트 내용")
       .userId("userId")
       .password(1234)
       .build();

   String content = objectMapper.writeValueAsString(postCreate);

   // expected
    this.mockMvc.perform(post("/posts")
              .contentType(MediaType.APPLICATION_JSON)
              .content(content))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.validation.title").value("제목엔 '테스트'가 포함될 수 없습니다."));
  }

  @DisplayName("게시글 조회시 count가 1 증가한다.")
  @Test
  void count_test() throws Exception {
    // given
    Post post = Post.builder()
        .title("제목")
        .content("내용")
        .password(1234)
        .build();
    postRepository.save(post);

    // when
    this.mockMvc.perform(get("/posts/{postId}", post.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // then
    int afterCount = postRepository.findById(post.getId())
        .orElseThrow(() -> new PostNotFoundException()).getCount();
    assertThat(afterCount).isGreaterThan(0);
  }

  @DisplayName("title=mymy10 조회시 제목이 mymy10가 포함된 게시글만 조회된다.")
  @Test
  void search_test() throws Exception{
    // given
    postRepository.saveAll(IntStream.range(0, 20)
        .mapToObj(i -> Post.builder()
            .title("mymy-"+i)
            .content("content")
            .userId("사용자")
            .password(1234).build())
        .collect(Collectors.toList())
    );

    // expected
    MvcResult result =
      this.mockMvc.perform(get("/posts?kw={kw}&kw_opt={kw_opt}", "mymy-10", "title"))
          .andDo(print())
          .andExpect(status().isOk())
          .andReturn();

    PageMaker<PostResponse> response = (PageMaker<PostResponse>) result.getModelAndView().getModel().get("response");
    assertThat(response.getResult().getContent().get(0).getTitle()).isEqualTo("mymy-10");
  }
}