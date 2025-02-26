package com.weatherapp.authentication_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaAuthResponse {
    private String email;
    private boolean isAuthenticated;
}
