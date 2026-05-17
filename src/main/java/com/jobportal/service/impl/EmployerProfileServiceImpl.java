package com.jobportal.service.impl;

import com.jobportal.entity.EmployerProfile;
import com.jobportal.entity.User;
import com.jobportal.repository.EmployerProfileRepository;
import com.jobportal.service.EmployerProfileService;
import com.jobportal.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class EmployerProfileServiceImpl implements EmployerProfileService {

    private final EmployerProfileRepository employerProfileRepository;
    private final UserService userService;

    public EmployerProfileServiceImpl(EmployerProfileRepository employerProfileRepository, UserService userService) {
        this.employerProfileRepository = employerProfileRepository;
        this.userService = userService;
    }

    @Override
    public EmployerProfile createOrUpdateProfile(EmployerProfile profile) {
        return employerProfileRepository.save(profile);
    }

    @Override
    public EmployerProfile getProfileByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user.getEmployerProfile() == null) {
            throw new RuntimeException("Employer Profile not found for User ID: " + userId);
        }
        return user.getEmployerProfile();
    }
}
