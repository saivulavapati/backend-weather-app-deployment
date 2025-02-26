package com.weatherapp.authentication_service.controller;

import com.weatherapp.authentication_service.dto.AuthResponse;
import com.weatherapp.authentication_service.dto.LoginRequest;
import com.weatherapp.authentication_service.dto.LoginResponse;
import com.weatherapp.authentication_service.dto.MessageResponse;
import com.weatherapp.authentication_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("signin")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        LoginResponse response = new LoginResponse();
        response.setMessage(authResponse.getMessage());
        response.setStatus(authResponse.isStatus());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,authResponse.getJwtCookie().toString())
                .body(response);
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUsername(Authentication authentication) {
        String username = authService.getUsername(authentication);
        return username != null ? ResponseEntity.ok(Map.of("username",username)) : ResponseEntity.badRequest().body("No user authenticated");
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        ResponseCookie cookie = authService.signOut();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("Signout Successful"));
    }

}
