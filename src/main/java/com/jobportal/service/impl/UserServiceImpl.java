package com.jobportal.service.impl;

import com.jobportal.entity.User;
import com.jobportal.entity.EmployerProfile;
import com.jobportal.entity.enums.Role;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
}

@Override
public User registerUser(User user) {

    // Check duplicate email
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new RuntimeException("Email is already registered");
    }

    // Encrypt password
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // 🔥 AUTO CREATE EMPLOYER PROFILE
    if (user.getRole() == Role.EMPLOYER) {
        EmployerProfile profile = new EmployerProfile();
        profile.setUser(user);
        profile.setCompanyName("Default Company");

        // Link both sides
        user.setEmployerProfile(profile);
    }

    return userRepository.save(user);
}

@Override
public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
}

@Override
public User getUserById(Long id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
}

}
