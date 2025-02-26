package com.weatherapp.authentication_service.service;

import com.weatherapp.authentication_service.dto.AuthResponse;
import com.weatherapp.authentication_service.dto.KafkaAuthRequest;
import com.weatherapp.authentication_service.dto.KafkaAuthResponse;
import com.weatherapp.authentication_service.dto.LoginRequest;
import com.weatherapp.authentication_service.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    private KafkaAuthResponse kafkaResponse;

    @Override
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        KafkaAuthRequest request = new KafkaAuthRequest(loginRequest.getEmail(),
                loginRequest.getPassword());
        kafkaTemplate.send("auth-validation-request",request);
        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            throw new IllegalStateException("Timeout, Please Try again");
        }
        if(kafkaResponse==null){
            throw new UsernameNotFoundException("User doen't exist, Register");
        }
        if(!kafkaResponse.isAuthenticated()){
            throw new BadCredentialsException("Invalid Credentials");
        }
        setSecurityContext(loginRequest.getEmail());
        ResponseCookie cookie = jwtUtils.generateJwtCookie(loginRequest.getEmail());

        return new AuthResponse(true,"Login Successful",cookie);

    }

    @KafkaListener(topics = "auth-validation-response",groupId = "auth-service-group")
    public void consumeAuthResponse(KafkaAuthResponse response){
        this.kafkaResponse=response;
    }

    @Override
    public String getUsername(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    @Override
    public ResponseCookie signOut() {
        return jwtUtils.cleanJwtCookie();
    }

    private void setSecurityContext(String email) {
        UserDetails userDetails = User.withUsername(email)
                .password("")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
