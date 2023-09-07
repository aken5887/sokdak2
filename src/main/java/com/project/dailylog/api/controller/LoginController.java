package com.project.dailylog.api.controller;

import com.project.dailylog.api.request.Login;
import com.project.dailylog.api.response.SessionResponse;
import com.project.dailylog.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

  private final UserService userService;

  @PostMapping("/login")
  public SessionResponse login(@RequestBody Login login) {
    log.info("login : {}", login.toString());
    return userService.login(login);
  }
}
