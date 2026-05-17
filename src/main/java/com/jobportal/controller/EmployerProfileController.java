package com.jobportal.controller;

import com.jobportal.entity.EmployerProfile;
import com.jobportal.entity.User;
import com.jobportal.service.EmployerProfileService;
import com.jobportal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;
    private final UserService userService;

    public EmployerProfileController(EmployerProfileService employerProfileService, UserService userService) {
        this.employerProfileService = employerProfileService;
        this.userService = userService;
    }

    @GetMapping("/employer/profile")
    public String viewProfile(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        try {
            model.addAttribute("profile", employerProfileService.getProfileByUserId(user.getId()));
        } catch (RuntimeException e) {
            EmployerProfile profile = new EmployerProfile();
            profile.setUser(user);
            model.addAttribute("profile", profile);
        }
        return "dashboard/employer_profile";
    }

    @PostMapping("/employer/profile/update")
    public String createOrUpdateProfile(@ModelAttribute("profile") EmployerProfile profile, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        profile.setUser(user);
        employerProfileService.createOrUpdateProfile(profile);
        return "redirect:/employer/profile";
    }
}
