package com.project.dailylog.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dailylog.api.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  @DisplayName("/posts를 JSON객체로 POST요청하면 Status 200을 리턴한다.")
  void create() throws Exception {
    PostCreate postCreate = PostCreate.builder()
        .title("제목입니다.")
        .writer("작성자입니다.")
        .content("내용입니다.")
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);

    this.mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postCreateStr))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("/posts POST 요청시 title과 writer는 필수 값이다.")
  void create2() throws Exception {
    PostCreate postCreate = PostCreate.builder()
        .title(null)
        .writer(null)
        .content("내용입니다.")
        .build();
    String postCreateStr = objectMapper.writeValueAsString(postCreate);

    this.mockMvc.perform(post("/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(postCreateStr))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.writer").value("작성자는 필수입니다."));
  }
}