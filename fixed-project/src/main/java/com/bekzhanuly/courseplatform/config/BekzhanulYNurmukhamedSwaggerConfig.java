package com.bekzhanuly.courseplatform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI Configuration
 * Author: Bekzhanuly Nurmukhamed
 */
@Configuration
public class BekzhanulYNurmukhamedSwaggerConfig {

    @Bean
    public OpenAPI bekzhanulуNurmukhamedOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Online Course Platform API")
                .description(
                    "**Final Project — Online Course Platform**\n\n" +
                    "**Author:** Bekzhanuly Nurmukhamed\n\n" +
                    "A full-featured REST API for an online learning platform with:\n" +
                    "- JWT Authentication & Authorization\n" +
                    "- Course management (CRUD, pagination, search, filtering)\n" +
                    "- Enrollment management\n" +
                    "- Review & Rating system\n" +
                    "- File upload/download\n" +
                    "- Async email notifications\n\n" +
                    "**Roles:** `ROLE_STUDENT`, `ROLE_INSTRUCTOR`, `ROLE_ADMIN`\n\n" +
                    "To authenticate: call `/api/auth/login` or `/api/auth/register`, " +
                    "copy the `accessToken`, click **Authorize** and paste `Bearer <token>`."
                )
                .version("1.0.0")
                .contact(new Contact()
                    .name("Bekzhanuly Nurmukhamed")
                    .email("nurmukhamed@courseplatform.kz"))
                .license(new License().name("MIT"))
            )
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Enter JWT token obtained from /api/auth/login")
                )
            );
    }
}
