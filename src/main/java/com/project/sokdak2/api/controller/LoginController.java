package com.project.sokdak2.api.controller;

import com.google.common.net.HttpHeaders;
import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.domain.User;
import com.project.sokdak2.api.request.Login;
import com.project.sokdak2.api.response.SessionResponse;
import com.project.sokdak2.api.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

  private final UserService userService;
  private final AppConfig appConfig;

  @Value("${me.jwt}")
  private String storeSession;

  @GetMapping("/login")
  public String login(){
    return "/user/login";
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<String> login(@RequestBody Login login) {
    log.info("login : {}", login.toString());

    ResponseCookie responseCookie = null;

    if("false".equalsIgnoreCase(storeSession)){
      SessionResponse sessionResponse = userService.login(login);
      responseCookie = responseCookie(sessionResponse.getAccessToken());
    }else if("true".equalsIgnoreCase(storeSession)){
      User user = userService.loginUser(login);

      SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, +1);
      Date exprDate = cal.getTime();

      String jws = Jwts.builder()
          .setSubject(String.valueOf(user.getId()))
          .signWith(secretKey)
          .setIssuedAt(new Date())
          .setExpiration(exprDate)
          .compact();

      responseCookie = responseCookie(jws);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .build();
  }

  private ResponseCookie responseCookie(String sessionCookieValue){
    return ResponseCookie.from("SESSION", sessionCookieValue)
        .path("/")
        .httpOnly(true)
        .secure(false)
        .maxAge(Duration.ofDays(30))
        .sameSite("Strict")
        .build();
  }
}
