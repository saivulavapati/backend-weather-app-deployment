package com.weatherapp.authentication_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic authValidationRequest(){
        return new NewTopic("auth-validation-request",1,(short) 1);
    }

    @Bean
    public NewTopic authValidationResponse(){
        return new NewTopic("auth-validation-response",1,(short) 1);
    }
}
