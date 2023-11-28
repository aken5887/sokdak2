package com.project.sokdak2.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.domain.Session;
import com.project.sokdak2.api.domain.User;
import com.project.sokdak2.api.repository.UserRepository;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

  @DisplayName("로그인 후 /admin 요청시 Session Id 및 User 정보를 반환한다.")
  @Test
  void admin() throws Exception {
    //given
    User user = User.builder()
            .name("test")
            .password("1234")
            .email("user@naver.com")
            .build();
    userRepository.save(user);

    LocalDate exprDate = LocalDate.now().plusMonths(1L);

    String jws = Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .setExpiration(Date.valueOf(exprDate))
            .signWith( Keys.hmacShaKeyFor(appConfig.getJwtKey()))
            .compact();

    Cookie sessionCookie = new Cookie("SESSION", jws);

    //expected
    this.mockMvc.perform(get("/admin")
            .cookie(sessionCookie)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.name").value(user.getName()));
  }
}