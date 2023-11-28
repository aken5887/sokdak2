package com.project.sokdak2.api.config;

import com.project.sokdak2.api.repository.SessionRepository;
import java.util.List;

import com.project.sokdak2.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final UserRepository userRepository;
  private final SessionRepository sessionRepository;
  @Value("${me.jwt}") private String jwtUse;
  private final AppConfig appConfig;

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "/posts");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AdminInterceptor())
        .addPathPatterns("/admin/interceptor");
  }


  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new UserArgumentResolver(userRepository, sessionRepository, appConfig, jwtUse));
  }
}
