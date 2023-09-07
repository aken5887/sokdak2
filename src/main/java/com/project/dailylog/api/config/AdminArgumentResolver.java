package com.project.dailylog.api.config;

import com.project.dailylog.api.config.annotation.Auth;
import com.project.dailylog.api.domain.Session;
import com.project.dailylog.api.exception.UnAuthorizedException;
import com.project.dailylog.api.repository.SessionRepository;
import com.project.dailylog.api.request.SessionUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AdminArgumentResolver implements HandlerMethodArgumentResolver {

  private final SessionRepository sessionRepository;
  private final String jwtUse;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    log.info("parameterType : "+ parameter.getParameterType());
    log.info("parameterAnnotation : "+ parameter.getParameterAnnotation(Auth.class));

    return parameter.getParameterType().equals(SessionUser.class)
        && parameter.getParameterAnnotation(Auth.class) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

    if(servletRequest == null){
      throw new UnAuthorizedException("servlet request is null");
    }

    Cookie[] cookies = servletRequest.getCookies();
    if(cookies == null || cookies.length == 0){
      throw new UnAuthorizedException("cookie가 존재하지 않습니다.");
    }

    Cookie sessionCookie = Arrays.stream(cookies)
        .filter(cookie -> cookie.getName().equals("SESSION"))
        .findFirst()
        .orElseThrow(() -> new UnAuthorizedException());

    String cookieValue = sessionCookie.getValue();

    if("false".equalsIgnoreCase(jwtUse)){
      // cookieValue -> accessToken 값
      // DB를 통한 검증
      Session findSession = sessionRepository.findByAccessToken(cookieValue)
          .orElseThrow(() -> new UnAuthorizedException());

      return findSession.toSessionUser();

    }else if("true".equalsIgnoreCase(jwtUse)){
      // cookieValue jwt 값
      log.info("------------- jwtKey : {}", JwtKey.getStrKey());
      byte[] decodedKey = Base64.decodeBase64(JwtKey.getStrKey());

      try{
        Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(decodedKey)
            .build()
            .parseClaimsJws(cookieValue);

        String sessionId = claims.getBody().getSubject();

        return SessionUser.builder()
            .id(Long.parseLong(sessionId))
            .build();
      }catch(JwtException e){
        log.error("JWT Token이 올바르지 않습니다.");
      }
    }
    throw new UnAuthorizedException();
  }
}
