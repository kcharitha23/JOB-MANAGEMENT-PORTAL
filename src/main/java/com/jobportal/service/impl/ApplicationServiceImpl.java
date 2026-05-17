package com.jobportal.service.impl;

import com.jobportal.entity.Application;
import com.jobportal.entity.enums.ApplicationStatus;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.service.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application applyForJob(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public List<Application> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    @Override
    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    @Override
    public Application updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));

        application.setStatus(status);
        return applicationRepository.save(application);
    }
}