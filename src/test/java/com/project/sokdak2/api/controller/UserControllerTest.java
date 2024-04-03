package com.project.sokdak2.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.domain.user.Role;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  SimpleMessageListenerContainer simpleMessageListenerContainer;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${me.jwt}")
  String jwtUse;

  @Autowired
  AppConfig appConfig;

  @BeforeEach
  void clean() {
    userRepository.deleteAll();
  }

  private User getUser() {
    return User.builder()
        .password("1234")
        .name("테스트유저")
        .role(Role.GENERAL)
        .build();
  }
}