package com.jobportal.controller;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.entity.enums.ApplicationStatus;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import com.jobportal.util.FileStorageUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    public ApplicationController(ApplicationService applicationService,
                                 JobService jobService,
                                 UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }

    // APPLY FORM
    @GetMapping("/apply/{jobId}")
    public String applyForJobForm(@PathVariable Long jobId, Model model) {
        model.addAttribute("jobId", jobId);
        model.addAttribute("application", new Application());
        return "application/apply";
    }

    // APPLY SUBMIT
    @PostMapping("/apply/{jobId}")
    public String applyForJob(
            @PathVariable Long jobId,
            @ModelAttribute("application") Application application,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            Principal principal
    ) {

        try {
            // 1. Logged-in user
            String email = principal.getName();
            User user = userService.getUserByEmail(email);

            // 2. Job
            Job job = jobService.getJobById(jobId);

            // 3. Save file
            String filePath = FileStorageUtil.saveFile(resumeFile, "uploads");

            // 4. Set required fields
            application.setStudent(user);
            application.setJob(job);
            application.setResumeUrl(filePath);

            // 5. Save
            applicationService.applyForJob(application);

            return "redirect:/applications/student/" + user.getId();

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/jobs?error";
        }
    }

    // STUDENT VIEW
    @GetMapping("/student/{studentId}")
    public String viewApplicationsByStudent(@PathVariable Long studentId, Model model) {
        model.addAttribute("applications", applicationService.getApplicationsByStudent(studentId));
        return "application/student_list";
    }

    // EMPLOYER VIEW
    @GetMapping("/job/{jobId}")
    public String viewApplicationsByJob(@PathVariable Long jobId, Model model) {
        model.addAttribute("applications", applicationService.getApplicationsByJob(jobId));
        return "application/job_list";
    }

    // UPDATE STATUS
    @PostMapping("/{applicationId}/status")
    public String updateApplicationStatus(@PathVariable Long applicationId,
                                          @RequestParam("status") ApplicationStatus status) {
        Application updatedApp = applicationService.updateApplicationStatus(applicationId, status);
        return "redirect:/applications/job/" + updatedApp.getJob().getId();
    }
}