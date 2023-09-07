package com.project.dailylog.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dailylog.api.domain.Session;
import com.project.dailylog.api.domain.User;
import com.project.dailylog.api.repository.SessionRepository;
import com.project.dailylog.api.repository.UserRepository;
import com.project.dailylog.api.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  SessionRepository sessionRepository;

  @BeforeEach
  void clean() {
    userRepository.deleteAll();
    sessionRepository.deleteAll();
  }

  private User getUser() {
    return User.builder()
        .userId("test")
        .password("1234")
        .name("테스트유저")
        .build();
  }

  private Login getLogin(User user){
    return Login.builder()
        .userId(user.getUserId())
        .password(user.getPassword())
        .build();
  }

  @DisplayName("/login 요청시 로그인이 정상적으로 된다.")
  @Test
  void login() throws Exception{
    // given
    User user = User.builder()
        .userId("test")
        .password("1234")
        .name("테스트유저")
        .build();

    userRepository.save(user);

    Login login = Login.builder()
        .userId(user.getUserId())
        .password(user.getPassword())
        .build();

    String loginStr = objectMapper.writeValueAsString(login);

    // expected
    this.mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(loginStr))
        .andExpect(status().isOk());

  }

  @DisplayName("로그인 성공 후 세션이 1개 생성된다.")
  @Transactional
  @Test
  void login2() throws Exception {
    // given
    User user = userRepository.save(getUser());
    String loginStr = objectMapper.writeValueAsString(getLogin(user));

    // expected
    this.mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginStr))
        .andExpect(status().isOk());

    // then
    assertThat(sessionRepository.count()).isGreaterThan(0);
  }

  @DisplayName("로그인 성공 후 세션 응답이 존재한다.")
  @Test
  void login_session_response() throws Exception {
    // given
    User user = userRepository.save(getUser());
    String loginStr = objectMapper.writeValueAsString(getLogin(user));

    // expected
    this.mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginStr))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").isNotEmpty());
  }

  @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다.")
  @Test
  void login_grant() throws Exception {
    // given
    User user = getUser();

    // when
    Session session = user.addSession(); // 세션 생성
    userRepository.save(user);

    // expected
    this.mockMvc.perform(get("/admin")
        .header("Authorization", session.getAccessToken()))
        .andExpect(status().isOk())
        .andExpect(content().string(Long.toString(session.getId())));
  }

  @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
  @Test
  void login_no_grant() throws Exception {
    // given
    User user = getUser();

    // when
    Session session = user.addSession(); // 세션 생성
    userRepository.save(user);

    // expected
    this.mockMvc.perform(get("/admin")
            .header("Authorization", "1234"))
        .andExpect(status().isUnauthorized());
  }

}