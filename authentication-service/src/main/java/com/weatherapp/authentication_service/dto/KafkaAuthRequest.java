package com.weatherapp.authentication_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaAuthRequest implements Serializable {
    private String email;
    private String password;
    private String requestId;
}
