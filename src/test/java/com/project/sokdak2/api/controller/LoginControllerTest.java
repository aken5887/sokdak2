package com.project.sokdak2.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.domain.user.Role;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.repository.SessionRepository;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  SimpleMessageListenerContainer simpleMessageListenerContainer;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  SessionRepository sessionRepository;

  @Value("${me.jwt}")
  String jwtUse;

  @Autowired
  AppConfig appConfig;

  @BeforeEach
  void clean() {
    userRepository.deleteAll();
    sessionRepository.deleteAll();
  }

  private User getUser() {
    return User.builder()
        .password("1234")
        .name("테스트유저")
        .role(Role.GENERAL)
        .build();
  }

  private Login getLogin(User user){
    return Login.builder()
        .password(user.getPassword())
        .build();
  }

  @DisplayName("/login 요청시 로그인이 정상적으로 된다.")
  @Test
  void login() throws Exception{
    // given
    User user = userRepository.save(getUser());

    Login login = Login.builder()
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
//        .andExpect(jsonPath("$.accessToken").isNotEmpty());
        .andExpect(cookie().exists("SESSION"));
  }
}