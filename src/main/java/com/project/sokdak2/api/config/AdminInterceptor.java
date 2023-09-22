package com.project.sokdak2.api.config;

import com.project.sokdak2.api.exception.UnAuthorizedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * /admin/create GET 검증
 */
public class AdminInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // 예외를 던질 condition
    String userId = request.getParameter("userId");
    String method = request.getMethod();
    if("GET".equalsIgnoreCase(method) && ObjectUtils.isEmpty(userId)){
      throw new UnAuthorizedException();
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
  }
}
