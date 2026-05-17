package com.jobportal.repository;

import com.jobportal.entity.Application;
import com.jobportal.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    
    List<Application> findByJobId(Long jobId);
    
    List<Application> findByStatus(ApplicationStatus status);
}
