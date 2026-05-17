package com.jobportal.service;

import com.jobportal.entity.User;

public interface UserService {
    User registerUser(User user);
    User getUserByEmail(String email);
    User getUserById(Long id);
}
