package com.project.sokdak2.api.config;

import com.project.sokdak2.api.domain.Visits;
import com.project.sokdak2.api.repository.VisitsRepository;
import com.project.sokdak2.api.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */

@RequiredArgsConstructor
public class VisitsInterceptor implements HandlerInterceptor {

  private final VisitsRepository visitsRepository;
  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
      throws Exception {
      Visits visits = Visits.builder()
              .ip(CommonUtil.getClientIp(req))
              .uri(req.getRequestURI())
              .method(req.getMethod())
              .build();
      visitsRepository.save(visits);

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
