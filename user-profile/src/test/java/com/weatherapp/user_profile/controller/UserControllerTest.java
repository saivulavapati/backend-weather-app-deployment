package com.weatherapp.user_profile.controller;

import com.weatherapp.user_profile.dto.UserDto;
import com.weatherapp.user_profile.entity.User;
import com.weatherapp.user_profile.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void register_ShouldReturnUserDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");

        when(userService.register(any(UserDto.class))).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.register(userDto);

        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).register(any(UserDto.class));
    }

    @Test
    void getUserByEmail_ShouldReturnUser() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<Optional<User>> response = userController.getUserByEmail("test@example.com");

        assertTrue(response.getBody().isPresent());
        assertEquals("test@example.com", response.getBody().get().getEmail());
    }
}
