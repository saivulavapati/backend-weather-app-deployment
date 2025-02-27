package com.weatherapp.user_profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaAuthResponse implements Serializable {

    private String email;
    private boolean authenticated;
    private String requestId;
    private String message;
}
