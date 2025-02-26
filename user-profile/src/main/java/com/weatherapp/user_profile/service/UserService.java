package com.weatherapp.user_profile.service;

import com.weatherapp.user_profile.dto.UserDto;
import com.weatherapp.user_profile.entity.User;

import java.util.Optional;

public interface UserService {
    UserDto register(UserDto userDto);
    Optional<User> getUserByEmail(String email);

}
