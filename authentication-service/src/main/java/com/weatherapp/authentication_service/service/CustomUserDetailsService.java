package com.weatherapp.authentication_service.service;

import com.weatherapp.authentication_service.client.UserProfileClient;
import com.weatherapp.authentication_service.dto.UserDto;
import com.weatherapp.authentication_service.jwt.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserProfileClient userProfileClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ResponseEntity<Optional<UserDto>> response = userProfileClient.getUserByEmail(email);
        Optional<UserDto> user = response.getBody();
        if(user.isPresent()){
            return new CustomUserDetails(user.get().getEmail(),
                    user.get().getPassword());
        }
        throw new UsernameNotFoundException("User Not Found");
    }
}