package com.jobportal.service.impl;

import com.jobportal.entity.StudentProfile;
import com.jobportal.entity.User;
import com.jobportal.repository.StudentProfileRepository;
import com.jobportal.service.StudentProfileService;
import com.jobportal.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserService userService;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository, UserService userService) {
        this.studentProfileRepository = studentProfileRepository;
        this.userService = userService;
    }

    @Override
    public StudentProfile createOrUpdateProfile(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }

    @Override
    public StudentProfile getProfileByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user.getStudentProfile() == null) {
            throw new RuntimeException("Student Profile not found for User ID: " + userId);
        }
        return user.getStudentProfile();
    }
}
