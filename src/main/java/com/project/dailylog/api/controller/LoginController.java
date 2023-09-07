package com.project.dailylog.api.controller;

import com.google.common.net.HttpHeaders;
import com.project.dailylog.api.config.JwtKey;
import com.project.dailylog.api.domain.User;
import com.project.dailylog.api.request.Login;
import com.project.dailylog.api.response.SessionResponse;
import com.project.dailylog.api.service.UserService;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${me.jwt}")
  private String storeSession;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Login login) {
    log.info("login : {}", login.toString());

    ResponseCookie responseCookie = null;

    if("false".equalsIgnoreCase(storeSession)){
      SessionResponse sessionResponse = userService.login(login);
      responseCookie = responseCookie(sessionResponse.getAccessToken());
    }else if("true".equalsIgnoreCase(storeSession)){
      User user = userService.loginUser(login);

      String jws = Jwts.builder()
          .setSubject(String.valueOf(user.getId()))
          .signWith(JwtKey.getKey())
          .compact();

      log.info(" ----- jwtKey : {}", JwtKey.getStrKey());
      responseCookie = responseCookie(jws);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .build();
  }

  private ResponseCookie responseCookie(String sessionCookieValue){
    return ResponseCookie.from("SESSION", sessionCookieValue)
        .domain("localhost")
        .path("/")
        .httpOnly(true)
        .secure(false)
        .maxAge(Duration.ofDays(30))
        .sameSite("Strict")
        .build();
  }
}
