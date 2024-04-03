package com.project.sokdak2.api.config.security;

import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.domain.user.UserPrincipal;
import com.project.sokdak2.api.exception.UserNotFoundException;
import com.project.sokdak2.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * author         : choi
 * date           : 2024-03-22
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {
    private final UserRepository userRepository;
    private final SimpleUrlAuthenticationFailureHandler failureHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((request) -> request
                .requestMatchers("/css/**", "/js/**", "/image/**","/favicon.ico").permitAll()
                .requestMatchers("/smartEditor/**","/tuiEditor/**","/tui-editor/**").permitAll()
                .requestMatchers("/","/posts/**","/error", "/user/**","/blog/**", "/password/**","/download/**").permitAll()
                .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/user/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureHandler(failureHandler)
                        .permitAll())
                .logout((logout) -> logout.permitAll()
                                        .logoutSuccessUrl("/"))
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .userDetailsService(userDetailsService(userRepository))
                .rememberMe(rm -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException());
            return new UserPrincipal(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
