package com.company.employeemanagement.service;

import com.company.employeemanagement.entity.User;
import com.company.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean validateLogin(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(User::getActive)
                .map(user -> passwordEncoder.matches(
                        rawPassword,
                        user.getPasswordHash()
                ))
                .orElse(false);
    }
}