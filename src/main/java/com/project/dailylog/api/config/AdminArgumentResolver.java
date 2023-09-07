package com.project.dailylog.api.config;

import com.project.dailylog.api.config.annotation.Auth;
import com.project.dailylog.api.domain.Session;
import com.project.dailylog.api.exception.UnAuthorizedException;
import com.project.dailylog.api.repository.SessionRepository;
import com.project.dailylog.api.request.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AdminArgumentResolver implements HandlerMethodArgumentResolver {

  private final SessionRepository sessionRepository;

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

    String authorization = webRequest.getHeader("Authorization");
    if(authorization == null || "".equalsIgnoreCase(authorization)){
      throw new UnAuthorizedException();
    }

    // DB를 통한 검증 추가
    Session findSession = sessionRepository.findByAccessToken(authorization)
        .orElseThrow(() -> new UnAuthorizedException());

    return findSession.toSessionUser();
  }
}
