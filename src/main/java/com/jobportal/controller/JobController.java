package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.entity.EmployerProfile;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jobs")
public class JobController {

private final JobService jobService;
private final UserService userService;

public JobController(JobService jobService, UserService userService) {
    this.jobService = jobService;
    this.userService = userService;
}

// View all jobs (also handles root "/jobs")
@GetMapping({"", "/"})
public String viewAllJobs(@RequestParam(defaultValue = "0") int page, Model model) {
    Pageable pageable = PageRequest.of(page, 1000);
    Page<Job> jobPage = jobService.getAllJobs(pageable);
    model.addAttribute("jobPage", jobPage);
    return "job/list";
}

// Search jobs
@GetMapping("/search")
public String searchJobs(@RequestParam("keyword") String keyword,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {

    Pageable pageable = PageRequest.of(page, 1000);
    Page<Job> jobPage = jobService.searchJobsByTitle(keyword, pageable);

    model.addAttribute("jobPage", jobPage);
    model.addAttribute("keyword", keyword);

    return "job/list";
}

// Filter jobs by location
@GetMapping("/filter")
public String filterJobs(@RequestParam("location") String location,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {

    Pageable pageable = PageRequest.of(page, 1000);
    Page<Job> jobPage = jobService.filterJobsByLocation(location, pageable);

    model.addAttribute("jobPage", jobPage);
    model.addAttribute("location", location);

    return "job/list";
}

// Show create form
@GetMapping("/create")
public String createJobForm(Model model) {
    model.addAttribute("job", new Job());
    return "job/create";
}

// CREATE JOB (FIXED)
@PostMapping("/create")
public String createJob(@ModelAttribute("job") Job job) {

    // Get logged-in user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    // Fetch user and employer profile
    User user = userService.getUserByEmail(email);
    EmployerProfile employerProfile = user.getEmployerProfile();

    // Safety check
    if (employerProfile == null) {
        throw new RuntimeException("Employer profile not found for this user");
    }

    // Attach employer to job
    job.setEmployerProfile(employerProfile);

    // Save job
    jobService.createJob(job);

    return "redirect:/jobs";
}

// Show edit form
@GetMapping("/{id}/edit")
public String updateJobForm(@PathVariable("id") Long id, Model model) {
    Job job = jobService.getJobById(id);
    model.addAttribute("job", job);
    return "job/edit";
}

// Update job
@PostMapping("/{id}/edit")
public String updateJob(@PathVariable("id") Long id,
                        @ModelAttribute("job") Job job) {

    jobService.updateJob(id, job);
    return "redirect:/jobs";
}

// Delete job
@PostMapping("/{id}/delete")
public String deleteJob(@PathVariable("id") Long id) {
    jobService.deleteJob(id);
    return "redirect:/jobs";
}
//MY JOBS (Employer Dashboard)
@GetMapping("/my-jobs")
public String myJobs(Model model) {

 // Get logged-in user
 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 String email = auth.getName();

 // Fetch user
 User user = userService.getUserByEmail(email);
 EmployerProfile employer = user.getEmployerProfile();

 // Get only this employer's jobs
 model.addAttribute("jobs", employer.getPostedJobs());

 return "job/my_jobs";
}

}
