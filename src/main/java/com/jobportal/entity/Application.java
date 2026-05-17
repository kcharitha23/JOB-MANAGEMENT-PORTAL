package com.jobportal.entity;

import com.jobportal.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @NotBlank(message = "Resume must be uploaded")
    @Column(name = "resume_url", nullable = false)
    private String resumeUrl;

    @Lob
    @Column(name = "cover_letter")
    private String coverLetter;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @CreationTimestamp
    @Column(name = "applied_date", updatable = false)
    private LocalDateTime appliedDate;
}
