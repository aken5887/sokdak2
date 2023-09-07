package com.project.dailylog.api.controller;

import com.google.common.net.HttpHeaders;
import com.project.dailylog.api.request.Login;
import com.project.dailylog.api.response.SessionResponse;
import com.project.dailylog.api.service.UserService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Login login) {
    log.info("login : {}", login.toString());
    SessionResponse sessionResponse = userService.login(login);
    ResponseCookie responseCookie = responseCookie(sessionResponse);

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .build();
  }

  private ResponseCookie responseCookie(SessionResponse sessionResponse){
    return ResponseCookie.from("SESSION", sessionResponse.getAccessToken())
        .domain("localhost")
        .path("/")
        .httpOnly(true)
        .secure(false)
        .maxAge(Duration.ofDays(30))
        .sameSite("Strict")
        .build();
  }
}
