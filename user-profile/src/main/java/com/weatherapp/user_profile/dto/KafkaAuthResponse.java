package com.weatherapp.user_profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaAuthResponse {

    private String email;
    private boolean isAuthenticated;
}
