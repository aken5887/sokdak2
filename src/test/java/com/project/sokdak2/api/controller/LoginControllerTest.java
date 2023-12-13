package com.project.sokdak2.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.domain.Session;
import com.project.sokdak2.api.domain.User;
import com.project.sokdak2.api.repository.SessionRepository;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.Login;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    User user = User.builder()
        .password("1234")
        .name("테스트유저")
        .build();

    userRepository.save(user);

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

  @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다.")
  @Test
  void login_grant() throws Exception {
    // given
    User user = getUser();

    // when
    userRepository.save(user);

    String cookieValue = "";
    if("false".equalsIgnoreCase(jwtUse)){
      Session session = user.addSession();
      cookieValue = session.getAccessToken();
    }else if("true".equalsIgnoreCase(jwtUse)){
      LocalDateTime  exprDateTime = LocalDateTime.now().plusMonths(1L);
      SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

      cookieValue = Jwts.builder()
              .setSubject(String.valueOf(user.getId()))
              .setExpiration(Date.valueOf(exprDateTime.toLocalDate()))
              .signWith(secretKey)
              .compact();
    }

    // 세션용 쿠키 생성
    Cookie sessionCookie = new Cookie("SESSION", cookieValue);

    // expected
    this.mockMvc.perform(get("/admin")
//        .header("Authorization", session.getAccessToken())
          .cookie(sessionCookie)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(user.getEmail()));
  }

}