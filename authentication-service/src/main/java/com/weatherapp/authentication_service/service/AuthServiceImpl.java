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

import java.util.UUID;
import java.util.concurrent.*;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private KafkaTemplate<String, KafkaAuthRequest> kafkaTemplate;


    private final ConcurrentHashMap<String, CompletableFuture<KafkaAuthResponse>>
            pendingRequests = new ConcurrentHashMap<>();

    @Override
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        String requestId = UUID.randomUUID().toString();
        KafkaAuthRequest request = new KafkaAuthRequest(loginRequest.getEmail(),
                loginRequest.getPassword(), requestId);

        CompletableFuture<KafkaAuthResponse> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaTemplate.send("auth-validation-request", request);

        try {
            KafkaAuthResponse response = future.get(15, TimeUnit.SECONDS);

            if (!response.isAuthenticated()) {
                if ("User Not Found".equals(response.getMessage())) {
                    throw new UsernameNotFoundException("User does not exist. Please register.");
                }
                throw new BadCredentialsException("Invalid Credentials");
            }
            setAuthenticationContext(loginRequest.getEmail());
            ResponseCookie cookie = jwtUtils.generateJwtCookie(loginRequest.getEmail());
            return new AuthResponse(true,"Login Successful",cookie);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IllegalStateException("Authentication timed out");
        } finally {
            pendingRequests.remove(requestId);
        }
    }

    @KafkaListener(topics = "auth-validation-response", groupId = "auth-service-group")
    public void consumeAuthResponse(KafkaAuthResponse response) {
        CompletableFuture<KafkaAuthResponse> future = pendingRequests.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }
    }

    @Override
    public String getUsername(Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return null;
    }

        @Override
        public ResponseCookie signOut() {
            return jwtUtils.cleanJwtCookie();
        }

    private void setAuthenticationContext(String email) {
        UserDetails userDetails = User.withUsername(email)
                .password("")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

}
