package com.weatherapp.authentication_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email Required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password Required")
    private String password;
}
