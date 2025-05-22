package com.farmmonitoring.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Base security configuration
        http
                .csrf(csrf -> csrf.disable())  // Для простоти демо-версії
                .authorizeHttpRequests(authz -> authz
                        // API endpoints вимагають авторизації
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                        // Swagger UI доступний усім
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"), 
                                        new AntPathRequestMatcher("/v3/api-docs/**"), 
                                        new AntPathRequestMatcher("/api-docs/**")).permitAll()
                        // Actuator endpoints для моніторингу
                        .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                        // H2 console
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()                        // Публічні статичні ресурси
                        .requestMatchers(new AntPathRequestMatcher("/css/**"), 
                                        new AntPathRequestMatcher("/js/**"), 
                                        new AntPathRequestMatcher("/img/**"),
                                        new AntPathRequestMatcher("/static/**"),
                                        new AntPathRequestMatcher("/favicon.ico"),
                                        new AntPathRequestMatcher("/error")).permitAll()
                        // Веб-інтерфейс вимагає авторизації
                        .requestMatchers(new AntPathRequestMatcher("/web/**")).authenticated()
                        // Дозволяємо доступ до OAuth2 ендпоінтів
                        .requestMatchers(new AntPathRequestMatcher("/oauth2/**"), 
                                        new AntPathRequestMatcher("/login/oauth2/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/web")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Для API використовуємо базову аутентифікацію
                .httpBasic(httpBasic -> {})
                // Disable X-Frame-Options for H2 Console
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
                
        // Add OAuth2 login only if we're not in a test environment
        boolean isTestEnvironment = Arrays.asList(environment.getActiveProfiles()).contains("test");
        if (!isTestEnvironment && oAuth2LoginSuccessHandler != null) {
            http.oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .successHandler(oAuth2LoginSuccessHandler)
            );
        }

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
