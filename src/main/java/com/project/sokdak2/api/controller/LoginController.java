package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.config.annotation.Users;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.request.Login;
import com.project.sokdak2.api.request.SessionUser;
import com.project.sokdak2.api.response.SessionResponse;
import com.project.sokdak2.api.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

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
    return "/login/login";
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<String> login(@RequestBody Login login) {
    log.debug("login : {}", login.toString());

    ResponseCookie responseCookie = null;

    if("false".equalsIgnoreCase(storeSession)){
      SessionResponse sessionResponse = userService.login(login);
      responseCookie = responseCookie(sessionResponse.getAccessToken());
    }else if("true".equalsIgnoreCase(storeSession)){
      User user = userService.loginUser(login);

      SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR, +1);
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

  @GetMapping("/logout")
  public String logout(@Users SessionUser sessionUser,
                       HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    Optional<Cookie> sessionCookie = Arrays.stream(cookies)
                  .filter(cookie -> cookie.getName().equals("SESSION"))
                  .findFirst();
    if(sessionCookie.isPresent()){
      Cookie cookieToDelete = sessionCookie.get();
      cookieToDelete.setMaxAge(0);
      cookieToDelete.setValue(null);
      cookieToDelete.setPath("/");
      response.addCookie(cookieToDelete);
    }
    return "redirect:/posts";
  }

  @GetMapping("/login/join")
  public String join() {
    return "/login/join";
  }

  private ResponseCookie responseCookie(String sessionCookieValue){
    return ResponseCookie.from("SESSION", sessionCookieValue)
        .path("/")
        .httpOnly(true)
        .secure(false)
        .maxAge(Duration.ofDays(1))
        .sameSite("Strict")
        .build();
  }
}
