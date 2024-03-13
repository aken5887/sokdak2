package com.project.sokdak2.api.config.argumentresolver;

import com.project.sokdak2.api.config.AppConfig;
import com.project.sokdak2.api.config.annotation.Users;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.exception.UnAuthorizedException;
import com.project.sokdak2.api.exception.UserNotFoundException;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserRepository userRepository;
  private final AppConfig appConfig;
  private final String jwtUse;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    log.debug("parameterType : "+ parameter.getParameterType());
    log.debug("parameterAnnotation : "+ parameter.getParameterAnnotation(Users.class));

    return parameter.getParameterType().equals(SessionUser.class)
        && parameter.getParameterAnnotation(Users.class) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

    if(servletRequest == null){
      throw new UnAuthorizedException("servlet request is null");
    }

    Cookie[] cookies = servletRequest.getCookies();
    if(cookies == null || cookies.length == 0 ){
      return null;
    }else {
        Optional<Cookie> sessionCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("SESSION"))
                .findFirst();
        if (sessionCookie.isEmpty()) return null;
        String userId = sessionCookie.get().getValue();
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException());
        SessionUser sessionUser = user.toSessionUser();
        mavContainer.addAttribute("sessionUser", sessionUser);
        return sessionUser;
    }
  }
}
