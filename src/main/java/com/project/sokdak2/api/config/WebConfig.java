package com.project.sokdak2.api.config;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.project.sokdak2.api.config.argumentresolver.UserArgumentResolver;
import com.project.sokdak2.api.config.interceptor.VisitsInterceptor;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.repository.VisitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final UserRepository userRepository;
  @Value("${me.jwt}") private String jwtUse;
  @Value("${spring.profiles.include}") private List<String> profiles;
  private final AppConfig appConfig;
  private final VisitsRepository visitsRepository;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addRedirectViewController("/", "/blog");
  }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(!profiles.contains("local")){
            registry.addInterceptor(new VisitsInterceptor(visitsRepository))
                    .addPathPatterns("/posts/**", "/password/**",
                            "/download/**", "/resume",
                            "/login", "/logout");
        }
    }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new UserArgumentResolver(userRepository, appConfig, jwtUse));
  }

  @Bean
  public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistrationBean() {
      FilterRegistrationBean<XssEscapeServletFilter> xxRegistrationBean = new FilterRegistrationBean<>();
      xxRegistrationBean.setFilter(new XssEscapeServletFilter());
      xxRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
      xxRegistrationBean.addUrlPatterns("/*");
      return xxRegistrationBean;
  }
}
