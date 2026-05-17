package com.jobportal.controller;

import com.jobportal.entity.User;
import com.jobportal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("applications", user.getApplications());
        return "dashboard/student";
    }

    @GetMapping("/employer/dashboard")
    public String employerDashboard(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("jobs", user.getEmployerProfile().getPostedJobs());
        return "dashboard/employer";
    }
}
