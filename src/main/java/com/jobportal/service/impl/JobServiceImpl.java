package com.jobportal.service.impl;

import com.jobportal.entity.Job;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, Job jobDetails) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));

        existingJob.setTitle(jobDetails.getTitle());
        existingJob.setDescription(jobDetails.getDescription());
        existingJob.setRequirements(jobDetails.getRequirements());
        existingJob.setLocation(jobDetails.getLocation());
        existingJob.setJobType(jobDetails.getJobType());
        existingJob.setSalaryRange(jobDetails.getSalaryRange());
        existingJob.setStatus(jobDetails.getStatus());

        return jobRepository.save(existingJob);
    }

    @Override
    public void deleteJob(Long id) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));

        jobRepository.delete(existingJob);
    }

    @Override
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    @Override
    public Page<Job> searchJobsByTitle(String title, Pageable pageable) {
        return jobRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Page<Job> filterJobsByLocation(String location, Pageable pageable) {
        return jobRepository.findByLocationContainingIgnoreCase(location, pageable);
    }

    // 🔥 ADD THIS METHOD
    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));
    }
}