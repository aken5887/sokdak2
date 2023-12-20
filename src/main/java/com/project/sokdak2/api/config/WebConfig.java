package com.project.sokdak2.api.config;

import com.project.sokdak2.api.repository.SessionRepository;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.repository.VisitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final UserRepository userRepository;
  private final SessionRepository sessionRepository;
  @Value("${me.jwt}") private String jwtUse;
  private final AppConfig appConfig;
  private final VisitsRepository visitsRepository;

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "/posts");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new VisitsInterceptor(visitsRepository))
            .addPathPatterns("/**")
            .excludePathPatterns("/","/js/**", "/css/**", "/image/**","/smartEditor/**");
  }


  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new UserArgumentResolver(userRepository, sessionRepository, appConfig, jwtUse));
  }
}
