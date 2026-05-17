package com.jobportal.repository;

import com.jobportal.entity.Job;
import com.jobportal.entity.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByLocationContainingIgnoreCase(String location, Pageable pageable);
    
    Page<Job> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    
    Page<Job> findByStatus(JobStatus status, Pageable pageable);
}
