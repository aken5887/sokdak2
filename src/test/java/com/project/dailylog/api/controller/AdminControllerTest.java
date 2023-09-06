package com.project.dailylog.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  @DisplayName("/admin GET 요청시 헤더에 Authorization이 null이면 401 오류가 발생한다.")
  @Test
  void admin_exception() throws Exception{
    // given
    // expected
    this.mockMvc.perform(get("/admin"))
      .andDo(print())
      .andExpect(status().isUnauthorized());
  }

  @DisplayName("/admin GET 요청시 accessToken을 반환한다.")
  @Test
  void admin() throws Exception {
    //given
    String accessToken = "12345";
    //expected
    this.mockMvc.perform(get("/admin")
        .header("Authorization", accessToken)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(accessToken));
  }
}