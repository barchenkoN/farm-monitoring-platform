package com.farmmonitoring.server.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@TestConfiguration
public class MockOAuth2Config {

    @Bean
    @Primary
    public AuthenticationSuccessHandler mockOAuth2LoginSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                              Authentication authentication) throws IOException, ServletException {
                response.sendRedirect("/web");
            }
        };
    }
}
