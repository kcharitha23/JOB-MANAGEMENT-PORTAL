package com.jobportal.entity;

import com.jobportal.entity.enums.JobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employer_profile_id", nullable = false)
    private EmployerProfile employerProfile;

    @NotBlank(message = "Job title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Job description is required")
    @Lob
    @Column(nullable = false)
    private String description;

    @Lob
    private String requirements;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotBlank(message = "Job type is required")
    @Column(name = "job_type", nullable = false)
    private String jobType;

    @Column(name = "salary_range")
    private String salaryRange;

    @CreationTimestamp
    @Column(name = "posted_date", updatable = false)
    private LocalDateTime postedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.ACTIVE;

    @ToString.Exclude
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Application> applications;
}
