package com.weatherapp.user_profile.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("User-Profile Service")
                        .version("1.0")
                        .description("User profile service for user registration")
                        .contact(new Contact().name("Sai Vulavapati")));
    }
}
