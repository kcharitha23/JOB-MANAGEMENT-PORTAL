package com.jobportal.service;

import com.jobportal.entity.Application;
import com.jobportal.entity.enums.ApplicationStatus;

import java.util.List;

public interface ApplicationService {

    Application applyForJob(Application application);

    List<Application> getApplicationsByStudent(Long studentId);

    List<Application> getApplicationsByJob(Long jobId);

    Application updateApplicationStatus(Long applicationId, ApplicationStatus status);
}