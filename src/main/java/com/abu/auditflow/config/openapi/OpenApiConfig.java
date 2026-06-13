package com.abu.auditflow.config.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    /*
     * Configures Swagger/OpenAPI.
     *
     * Adds:
     * - API metadata
     * - JWT Bearer authentication support
     */
    @Bean
    OpenAPI openAPI() {

        String securitySchemeName =
                "Bearer Authentication";

        return new OpenAPI()

                .info(new Info()

                        .title("AuditFlow API")

                        .version("1.0.0")

                        .description(
                                "Enterprise Spring Boot Template"))

                .addSecurityItem(

                        new SecurityRequirement()

                                .addList(
                                        securitySchemeName))

                .schemaRequirement(

                        securitySchemeName,

                        new SecurityScheme()

                                .name("Authorization")

                                .type(
                                        SecurityScheme.Type.HTTP)

                                .scheme("bearer")

                                .bearerFormat("JWT"));
    }
}