package com.project.dailylog.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class IndexControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("/posts/list 페이지가 정상적으로 호출된다.")
  void list() throws Exception {
    this.mockMvc.perform(get("/posts/list"))
        .andDo(print())
        .andExpect(status().isOk());
  }
}