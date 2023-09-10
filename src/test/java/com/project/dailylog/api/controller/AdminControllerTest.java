package com.project.dailylog.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.dailylog.api.config.AppConfig;
import com.project.dailylog.api.domain.Session;
import com.project.dailylog.api.domain.User;
import com.project.dailylog.api.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class AdminControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  AppConfig appConfig;

  @DisplayName("/admin/interceptor GET 요청시 userId가 null이면 401오류가 발생한다.")
  @Test
  void interceptor() throws Exception {
    // given
    // expected
    this.mockMvc.perform(get("/admin/interceptor")
        .param("userId", ""))
        .andExpect(status().isUnauthorized());
  }

  @DisplayName("/admin GET 요청시 헤더에 Authorization이 null이면 401 오류가 발생한다.")
  @Test
  void admin_exception() throws Exception{
    // given
    // expected
    this.mockMvc.perform(get("/admin"))
      .andDo(print())
      .andExpect(status().isUnauthorized());
  }

  @DisplayName("/admin GET 요청시 SESSION ID를 반환한다.")
  @Test
  void admin() throws Exception {
    //given
    User user = User.builder()
        .userId("test")
        .name("test")
        .password("1234")
        .build();
    Session session = user.addSession();
    userRepository.save(user);

    String jws = Jwts.builder()
        .setSubject(String.valueOf(session.getId()))
        .signWith( Keys.hmacShaKeyFor(appConfig.getJwtKey()))
        .compact();

    Cookie sessionCookie = new Cookie("SESSION", jws);

    //expected
    this.mockMvc.perform(get("/admin")
        .cookie(sessionCookie)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(Long.toString(session.getId())));
  }
}