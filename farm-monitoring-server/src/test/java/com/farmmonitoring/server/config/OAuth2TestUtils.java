package com.farmmonitoring.server.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Утилітний клас для тестування OAuth2 аутентифікації
 */
public class OAuth2TestUtils {

    /**
     * Створює OAuth2AuthenticationToken для тестування
     * 
     * @param authorityNames Ролі користувача
     * @param userAttributes Атрибути користувача
     * @param userNameAttributeName Назва атрибуту з ім'ям користувача
     * @return OAuth2AuthenticationToken
     */
    public static OAuth2AuthenticationToken createOAuth2Token(
            List<String> authorityNames,
            Map<String, Object> userAttributes,
            String userNameAttributeName,
            String registrationId) {

        Collection<GrantedAuthority> authorities = authorityNames.stream()
                .map(SimpleGrantedAuthority::new)
                .map(a -> (GrantedAuthority) a)
                .toList();

        OAuth2User user = new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);
        return new OAuth2AuthenticationToken(user, authorities, registrationId);
    }

    /**
     * Створює Google OAuth2 токен для тестування
     * 
     * @return OAuth2AuthenticationToken
     */
    public static OAuth2AuthenticationToken createGoogleToken() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "12345");
        attributes.put("name", "Test User");
        attributes.put("email", "testuser@example.com");
        attributes.put("picture", "https://example.com/picture.jpg");
        
        return createOAuth2Token(
                List.of("ROLE_USER"), 
                attributes, 
                "sub", 
                "google");
    }

    /**
     * Створює GitHub OAuth2 токен для тестування
     * 
     * @return OAuth2AuthenticationToken
     */
    public static OAuth2AuthenticationToken createGithubToken() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 12345);
        attributes.put("login", "testuser");
        attributes.put("name", "Test User");
        attributes.put("email", "testuser@example.com");
        attributes.put("avatar_url", "https://example.com/avatar.jpg");
        
        return createOAuth2Token(
                List.of("ROLE_USER"), 
                attributes, 
                "id", 
                "github");
    }
}
