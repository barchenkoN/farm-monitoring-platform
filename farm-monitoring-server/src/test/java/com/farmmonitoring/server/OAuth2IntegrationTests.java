package com.farmmonitoring.server;

import com.farmmonitoring.server.config.MockOAuth2Config;
import com.farmmonitoring.server.config.TestSecurityConfig;
import com.farmmonitoring.server.config.OAuth2TestUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, MockOAuth2Config.class})
public class OAuth2IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGoogleOAuth2Authentication() throws Exception {
        OAuth2AuthenticationToken googleToken = OAuth2TestUtils.createGoogleToken();

        mockMvc.perform(get("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.authentication(googleToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGithubOAuth2Authentication() throws Exception {
        OAuth2AuthenticationToken githubToken = OAuth2TestUtils.createGithubToken();

        mockMvc.perform(get("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.authentication(githubToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testWebInterfaceAccessWithOAuth2() throws Exception {
        OAuth2AuthenticationToken googleToken = OAuth2TestUtils.createGoogleToken();

        mockMvc.perform(get("/web")
                .with(SecurityMockMvcRequestPostProcessors.authentication(googleToken)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginPageAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}
