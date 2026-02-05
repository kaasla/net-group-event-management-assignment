package com.eventmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eventmanagement.dto.ErrorResponse;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Management API")
                        .version("1.0")
                        .description("API for managing events and participant registrations")
                        .contact(new Contact()
                                .name("Event Management")
                                .email("admin@eventmanagement.com")))
                .components(new Components()
                        .addResponses("InternalServerError", new ApiResponse()
                                .description("Internal server error")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new Schema<ErrorResponse>()
                                                        .$ref("#/components/schemas/ErrorResponse"))))));
    }
}
