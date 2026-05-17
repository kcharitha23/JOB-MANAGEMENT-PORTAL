package com.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "employer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Lob
    @Column(name = "company_description")
    private String companyDescription;

    private String website;
    
    private String location;
    
    @OneToMany(mappedBy = "employerProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Job> postedJobs;
}
