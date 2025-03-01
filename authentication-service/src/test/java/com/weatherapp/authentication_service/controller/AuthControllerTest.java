package com.weatherapp.authentication_service.controller;

import com.weatherapp.authentication_service.dto.AuthResponse;
import com.weatherapp.authentication_service.dto.LoginRequest;
import com.weatherapp.authentication_service.dto.LoginResponse;
import com.weatherapp.authentication_service.dto.MessageResponse;
import com.weatherapp.authentication_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private ResponseCookie responseCookie;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("sai@gmail.com", "sai@123");
        responseCookie = ResponseCookie.from("weather-jwt-cookie", "token123").build();
        authResponse = new AuthResponse(true, "Login Successful", responseCookie);
    }

    @Test
    void testSignIn_Success() {
        when(authService.authenticateUser(loginRequest)).thenReturn(authResponse);

        ResponseEntity<LoginResponse> responseEntity = authController.signIn(loginRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Login Successful", responseEntity.getBody().getMessage());
        assertTrue(responseEntity.getBody().getStatus());
        assertEquals(responseCookie.toString(), responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    @Test
    void testGetUsername_Authenticated() {
        when(authService.getUsername(authentication)).thenReturn("sai@gmail.com");

        ResponseEntity<?> responseEntity = authController.getUsername(authentication);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(Map.of("username", "sai@gmail.com"), responseEntity.getBody());
    }

    @Test
    void testGetUsername_NoAuthentication() {
        when(authService.getUsername(authentication)).thenReturn(null);

        ResponseEntity<?> responseEntity = authController.getUsername(authentication);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("No user authenticated", responseEntity.getBody());
    }

    @Test
    void testSignOut_Success() {
        when(authService.signOut()).thenReturn(responseCookie);

        ResponseEntity<?> responseEntity = authController.signOut();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Signout Successful", ((MessageResponse) responseEntity.getBody()).getMessage());
        assertEquals(responseCookie.toString(), responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }
}
