package com.weatherapp.user_profile.service;

import com.weatherapp.user_profile.dto.KafkaAuthRequest;
import com.weatherapp.user_profile.dto.KafkaAuthResponse;
import com.weatherapp.user_profile.entity.User;
import com.weatherapp.user_profile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaUserProfileService {

    @Autowired
    private KafkaTemplate<String, KafkaAuthResponse> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @KafkaListener(topics = "auth-validation-request", groupId = "user-profile-group")
    public void consumeKafkaAuthRequest(KafkaAuthRequest kafkaAuthRequest) {
        KafkaAuthResponse kafkaAuthResponse = validateUser(kafkaAuthRequest);
        kafkaTemplate.send("auth-validation-response", kafkaAuthResponse);
    }

    private KafkaAuthResponse validateUser(KafkaAuthRequest kafkaAuthRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(kafkaAuthRequest.getEmail());
        if (optionalUser.isEmpty()) {
            return new KafkaAuthResponse(kafkaAuthRequest.getEmail(), false, kafkaAuthRequest.getRequestId(), "User Not Found");
        }
        boolean isAuthenticated = passwordEncoder.matches(kafkaAuthRequest.getPassword(), optionalUser.get().getPassword());
        return new KafkaAuthResponse(kafkaAuthRequest.getEmail(), isAuthenticated, kafkaAuthRequest.getRequestId(), isAuthenticated ? "Success" : "Invalid Credentials");
    }
}
