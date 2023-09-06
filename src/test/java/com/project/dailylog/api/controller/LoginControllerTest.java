package com.project.dailylog.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dailylog.api.domain.User;
import com.project.dailylog.api.repository.UserRepository;
import com.project.dailylog.api.request.Login;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @DisplayName("/login 요청시 User 정보가 리턴된다.")
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
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(user.getName()));

  }
}