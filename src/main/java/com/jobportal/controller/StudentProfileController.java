package com.jobportal.controller;

import com.jobportal.entity.StudentProfile;
import com.jobportal.entity.User;
import com.jobportal.service.StudentProfileService;
import com.jobportal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentProfileController {

    private final StudentProfileService studentProfileService;
    private final UserService userService;

    public StudentProfileController(StudentProfileService studentProfileService, UserService userService) {
        this.studentProfileService = studentProfileService;
        this.userService = userService;
    }

    @GetMapping("/student/profile")
    public String viewProfile(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        try {
            model.addAttribute("profile", studentProfileService.getProfileByUserId(user.getId()));
        } catch (RuntimeException e) {
            StudentProfile profile = new StudentProfile();
            profile.setUser(user);
            model.addAttribute("profile", profile);
        }
        return "dashboard/student_profile";
    }

    @PostMapping("/student/profile/update")
    public String createOrUpdateProfile(@ModelAttribute("profile") StudentProfile profile, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        profile.setUser(user);
        studentProfileService.createOrUpdateProfile(profile);
        return "redirect:/student/profile";
    }
}
