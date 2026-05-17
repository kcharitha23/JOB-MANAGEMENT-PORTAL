package com.jobportal.service;

import com.jobportal.entity.StudentProfile;

public interface StudentProfileService {
    StudentProfile createOrUpdateProfile(StudentProfile profile);
    StudentProfile getProfileByUserId(Long userId);
}
