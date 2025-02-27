package com.weatherapp.authentication_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaAuthResponse implements Serializable {
    private String email;
    private boolean authenticated;
    private String requestId;
    private String message;
}
