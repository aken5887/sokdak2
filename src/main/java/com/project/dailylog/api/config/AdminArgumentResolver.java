package com.project.dailylog.api.config;

import com.project.dailylog.api.exception.UnAuthorizedException;
import com.project.dailylog.api.request.SessionUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AdminArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(SessionUser.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    String authorization = webRequest.getHeader("Authorization");
    if(authorization == null || "".equalsIgnoreCase(authorization)){
      throw new UnAuthorizedException();
    }
    return SessionUser.builder().id(Long.parseLong(authorization)).build();
  }
}
