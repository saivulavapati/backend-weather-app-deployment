package com.weatherapp.authentication_service.client;


import com.weatherapp.authentication_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "user-profile-service",url = "http://localhost:8081")
public interface UserProfileClient {

    @GetMapping("/api/v1/users/email")
    ResponseEntity<Optional<UserDto>> getUserByEmail(@RequestParam String email);
}