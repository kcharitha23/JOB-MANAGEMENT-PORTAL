package com.jobportal.service;

import com.jobportal.entity.EmployerProfile;

public interface EmployerProfileService {
    EmployerProfile createOrUpdateProfile(EmployerProfile profile);
    EmployerProfile getProfileByUserId(Long userId);
}
