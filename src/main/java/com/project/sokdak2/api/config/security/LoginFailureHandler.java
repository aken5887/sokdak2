package com.project.sokdak2.api.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * author         : choi
 * date           : 2024-04-03
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        int errorCode = 0;
        if(exception instanceof InternalAuthenticationServiceException) {
            errorCode = 1;
        } else if(exception instanceof BadCredentialsException) {
            errorCode = 2;
        } else if (exception instanceof UsernameNotFoundException){
            errorCode = 3;
        } else {
            errorCode = 4;
        }
        setDefaultFailureUrl("/user/login?errorCode="+errorCode);
        super.onAuthenticationFailure(request, response, exception);
    }
}
