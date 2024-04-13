package com.project.sokdak2.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.domain.user.Role;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.exception.PostNotFoundException;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.PostCreate;
import com.project.sokdak2.api.request.PostEdit;
import com.project.sokdak2.api.response.PostResponse;
import com.project.sokdak2.api.util.PageMaker;
import jakarta.servlet.http.Cookie;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  SimpleMessageListenerContainer simpleMessageListenerContainer;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  PostRepository postRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  AppConfig appConfig;

  @BeforeEach
  void setup() {
    postRepository.deleteAll();
  }

  @Test
  @Transactional
  @DisplayName("/posts를 요청하면 Status 200을 리턴한다.")
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
    this.mockMvc.perform(createPostRequest("/posts", postCreate))
        
        .andExpect(status().isOk());
//        .andExpect(status().is3xxRedirection())
//        .andExpect(view().name(StringContains.containsString("/posts/")))
//        .andExpect(flash().attributeExists("response"));
  }

  @Test
  @Transactional
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
    this.mockMvc.perform(createPostRequest("/posts", postCreate))
        
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
      this.mockMvc.perform(createPostRequest("/posts", postCreate))
        
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
      this.mockMvc.perform(get("/posts?page=2&dir_props=id&dir=desc")
          .contentType(MediaType.APPLICATION_JSON))
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
    // expected
    this.mockMvc.perform(patch("/posts")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .param("id", post.getId().toString())
                    .param("title","제목(수정)")
                    .param("content","내용(수정)")
                    .param("password","1234"))
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
        .password(12345)
        .build();
    postRepository.save(post);

    PostEdit postEdit = PostEdit
        .builder()
        .password(12345)
        .build();

    // when
    this.mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postEdit)))
            
            .andExpect(status().isOk());
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/posts"));

    // then
    assertThat(postRepository.count()).isEqualTo(0);
  }

  @DisplayName("존재하지 않는 게시글을 조회하면 404에러 코드와 함께 /errors 페이지를 리턴된다.")
  @Test
  void get_not_found() throws Exception {
    // expected
    this.mockMvc.perform(get("/posts/1")
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(model().attribute("error",
                    hasProperty("code", is("404"))))
            .andExpect(view().name("/errors/error"));
  }

  @DisplayName("존재하지 않는 게시글을 수정하면 404오류가 발생한다.")
  @Test
  void edit_not_found() throws Exception {
    // given
    PostEdit postEdit = PostEdit.builder()
        .id(1L)
        .title("제목")
        .content("내용")
        .password(1234)
        .build();

    String content = objectMapper.writeValueAsString(postEdit);

    // expected
    this.mockMvc.perform(patch("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isBadRequest());
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
          .andExpect(status().isOk())
          .andReturn();

    PageMaker<PostResponse> response = (PageMaker<PostResponse>) result.getModelAndView().getModel().get("response");
    assertThat(response.getResult().getContent().get(0).getTitle()).isEqualTo("mymy-10");
  }

  private MockHttpServletRequestBuilder createPostRequest(String url, PostCreate postCreate){
    return post(url)
        .param("title", postCreate.getTitle())
        .param("userId", postCreate.getUserId())
        .param("content", postCreate.getContent())
        .param("password", String.valueOf(postCreate.getPassword()));
  }

  @DisplayName("익명글은 password 페이지로 forward 된다.")
  @Test
  void test6() throws Exception {
    Post post = Post.builder()
            .title("제목")
            .content("내용")
            .password(1234)
            .locked(1)
            .build();
    postRepository.save(post);

    // expected
    this.mockMvc.perform(get("/posts/{postId}", post.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            
            .andExpect(status().isOk())
            .andExpect(forwardedUrl(("/password/"+post.getId()+"?reqType=2")));
  }

  @DisplayName("관리자는 비밀번호 없이 익명글을 읽을 수 있다.")
//  @Test
  // TODO 스프링 시큐리티 적용 후 테스트 코드 다시 만들기
  void test7() throws Exception{
    // given
    Post post = Post.builder()
            .title("익명글")
            .userId("익명 작성자")
            .password(1234)
            .locked(1)
            .build();
    postRepository.save(post);

    User user = User.builder()
            .email("abc@test.com")
            .name("관리자")
            .password("abcd")
            .role(Role.ADMIN)
            .build();
    userRepository.save(user);

    Cookie sessionCookie = new Cookie("SESSION", String.valueOf(user.getId()));

    // expected
    this.mockMvc.perform(get("/posts/{postId}", post.getId())
                    .cookie(sessionCookie))
            .andExpect(status().isOk())
            .andExpect(view().name("/posts/view"));
  }

  @DisplayName("일반사용자는 비밀번호를 입력해야 익명글을 읽을 수 있다.")
  @Test
  void test8() throws Exception{
    // given
    Post post = Post.builder()
            .title("익명글")
            .userId("익명 작성자")
            .password(1234)
            .locked(1)
            .build();
    postRepository.save(post);

    User user = User.builder()
            .email("abcd@test.com")
            .name("일반")
            .password("abcd")
            .role(Role.GENERAL)
            .build();
    userRepository.save(user);

    Cookie sessionCookie = new Cookie("SESSION", String.valueOf(user.getId()));

    // expected
    this.mockMvc.perform(get("/posts/{postId}", post.getId())
                    .cookie(sessionCookie))
            .andExpect(status().isOk())
            .andExpect(view().name(containsString("/password/"+post.getId())));
  }

  @DisplayName("글 제목에 <script> 태그가 들어가면 escape letter로 치환된다.")
  @Test
  void test9() throws Exception{
    //given
    String xssTitle = "시작<script>document.cookie</script>끝";
    String expected = "시작&lt;script&gt;document.cookie&lt;/script&gt;끝";
    //when
    this.mockMvc.perform(post("/posts")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .param("title", xssTitle)
            .param("userId", "test")
            .param("password", "1234")
            .param("content","1234"))
            
            .andExpect(status().isOk());
    //then
    Post post = postRepository.findAll().get(0);
    assertThat(post.getTitle()).isEqualTo(expected);
  }

  @DisplayName("글 내용에 <p>,<h1> 태그가 들어가면 escape letter로 치환되지 않는다..")
  @Test
  void test10() throws Exception{
    //given
    String xssContent = "<p><b><span style=\"font-size: 14pt;\">제목</span></b></p>\n";
    //when
    this.mockMvc.perform(post("/posts")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .param("title", "XSS test2")
                    .param("userId", "test")
                    .param("password", "1234")
                    .param("content",xssContent))
            
            .andExpect(status().isOk());
    //then
    Post post = postRepository.findAllByTitle("XSS test2").get(0);
    assertThat(post.getContent()).isEqualTo(xssContent);
  }
}