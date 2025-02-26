package com.weatherapp.user_profile.service;

import com.weatherapp.user_profile.dto.UserDto;
import com.weatherapp.user_profile.entity.User;
import com.weatherapp.user_profile.exception.EmailAlreadyExistException;
import com.weatherapp.user_profile.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto register(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());
        if(userByEmail.isPresent()){
            throw new EmailAlreadyExistException("Email already exist");
        }
        User user = mapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
