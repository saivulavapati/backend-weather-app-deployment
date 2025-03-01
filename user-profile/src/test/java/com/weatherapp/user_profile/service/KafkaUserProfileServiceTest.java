package com.weatherapp.user_profile.service;

import com.weatherapp.user_profile.dto.KafkaAuthRequest;
import com.weatherapp.user_profile.dto.KafkaAuthResponse;
import com.weatherapp.user_profile.entity.User;
import com.weatherapp.user_profile.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaUserProfileServiceTest {

    @InjectMocks
    private KafkaUserProfileService kafkaUserProfileService;

    @Mock
    private KafkaTemplate<String, KafkaAuthResponse> kafkaTemplate;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private KafkaAuthRequest kafkaAuthRequest;
    private User mockUser;
    private String requestId = "req-123";

    @BeforeEach
    void setUp() {
        kafkaAuthRequest = new KafkaAuthRequest("test@example.com", "password123", requestId);
        mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
    }

    @Test
    void testConsumeKafkaAuthRequest_UserExistsAndAuthenticated() {
        when(userRepository.findByEmail(kafkaAuthRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(kafkaAuthRequest.getPassword(), mockUser.getPassword())).thenReturn(true);

        kafkaUserProfileService.consumeKafkaAuthRequest(kafkaAuthRequest);

        verify(kafkaTemplate, times(1)).send(eq("auth-validation-response"), any(KafkaAuthResponse.class));
    }

    @Test
    void testConsumeKafkaAuthRequest_UserExistsButInvalidPassword() {
        when(userRepository.findByEmail(kafkaAuthRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(kafkaAuthRequest.getPassword(), mockUser.getPassword())).thenReturn(false);

        kafkaUserProfileService.consumeKafkaAuthRequest(kafkaAuthRequest);

        verify(kafkaTemplate, times(1)).send(eq("auth-validation-response"), argThat(response ->
                !response.isAuthenticated() && "Invalid Credentials".equals(response.getMessage())
        ));
    }

    @Test
    void testConsumeKafkaAuthRequest_UserNotFound() {
        when(userRepository.findByEmail(kafkaAuthRequest.getEmail())).thenReturn(Optional.empty());

        kafkaUserProfileService.consumeKafkaAuthRequest(kafkaAuthRequest);

        verify(kafkaTemplate, times(1)).send(eq("auth-validation-response"), argThat(response ->
                !response.isAuthenticated() && "User Not Found".equals(response.getMessage())
        ));
    }
}
