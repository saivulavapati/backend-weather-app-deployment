package com.weatherapp.authentication_service.service;

import com.weatherapp.authentication_service.dto.AuthResponse;
import com.weatherapp.authentication_service.dto.LoginRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

public interface AuthService {

    AuthResponse authenticateUser(LoginRequest loginRequest);

    String getUsername(Authentication authentication);

    ResponseCookie signOut();
}
