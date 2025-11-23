// src/main/java/org/example/abc/service/AuthService.java
package org.example.abc.service;

import org.example.abc.dto.LoginRequest;
import org.example.abc.dto.RegisterRequest;
import org.example.abc.model.User;
import org.example.abc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return false;
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean authenticate(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElse(false);
    }

    public Optional<Integer> authenticateAndGetUserId(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(User::getId);
    }

    public Optional<User> authenticateAndGetUser(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()));
    }
}