package com.project.sokdak2.api.config;

import com.project.sokdak2.api.config.annotation.Auth;
import com.project.sokdak2.api.domain.Session;
import com.project.sokdak2.api.domain.User;
import com.project.sokdak2.api.exception.UnAuthorizedException;
import com.project.sokdak2.api.exception.UserNotFoundException;
import com.project.sokdak2.api.repository.SessionRepository;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.SessionUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserRepository userRepository;
  private final SessionRepository sessionRepository;
  private final AppConfig appConfig;
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
      log.info("------------- jwtKey : {}", appConfig.getJwtKey());
      try{
        Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(appConfig.getJwtKey())
            .build()
            .parseClaimsJws(cookieValue);

        String userId = claims.getBody().getSubject();
        Date exprDate = claims.getBody().getExpiration();

        if(exprDate == null || exprDate.before(new Date())) {
          log.error("올바르지 않은 Token 값 입니다.");
          throw new UnAuthorizedException();
        }

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(()-> new UserNotFoundException());
        Session session = user.addSession();
        SessionUser sessionUser = session.toSessionUser();

        mavContainer.addAttribute("sessionUser", sessionUser);

        return sessionUser;
      }catch(JwtException e){
        log.error("JWT Token이 올바르지 않습니다.");
      }
    }
    throw new UnAuthorizedException();
  }
}
