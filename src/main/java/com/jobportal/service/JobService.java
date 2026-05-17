package com.jobportal.service;

import com.jobportal.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {
    Job createJob(Job job);
    Job updateJob(Long id, Job jobDetails);
    void deleteJob(Long id);
    Page<Job> getAllJobs(Pageable pageable);
    Page<Job> searchJobsByTitle(String title, Pageable pageable);
    Page<Job> filterJobsByLocation(String location, Pageable pageable);
	Job getJobById(Long id);
}
