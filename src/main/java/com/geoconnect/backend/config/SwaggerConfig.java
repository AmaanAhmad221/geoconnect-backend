package com.geoconnect.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("GeoConnect API")
                .description(
                    "Real-Time Service Marketplace API — " +
                    "Connect customers with nearby service providers"
                )
                .version("1.0.0")
                .contact(new Contact()
                    .name("Amaan")
                    .email("amaan@geoconnect.com")
                )
            )
            //  JWT Authentication in Swagger
            .addSecurityItem(new SecurityRequirement()
                .addList("Bearer Authentication")
            )
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter your JWT token here")
                )
            );
    }
}