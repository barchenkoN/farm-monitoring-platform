package com.farmmonitoring.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Farm Monitoring Platform API")
                        .version("1.0.0")
                        .description("API для системи моніторингу фермерських операцій. " +
                                "Дозволяє керувати фермами, полями, датчиками та отримувати показники з них.")
                        .contact(new Contact()
                                .name("Farm Monitoring Team")
                                .email("support@farmmonitoring.com")
                                .url("https://farmmonitoring.com"))
                )
                .servers(Arrays.asList(
                        new Server().url("/").description("Default Server URL")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization"))
                )
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
