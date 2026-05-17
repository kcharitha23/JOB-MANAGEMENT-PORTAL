package com.jobportal.controller;

import com.jobportal.entity.User;
import com.jobportal.entity.enums.Role;
import com.jobportal.service.OtpService;
import com.jobportal.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;

    public AuthController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, Model model, HttpSession session) {
        try {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new RuntimeException("Email is required");
            }

            String otp = otpService.generateOtp();
            session.setAttribute("pendingFirstName", user.getFirstName());
            session.setAttribute("pendingLastName", user.getLastName());
            session.setAttribute("pendingEmail", user.getEmail());
            session.setAttribute("pendingPassword", user.getPassword());
            session.setAttribute("pendingRole", user.getRole());
            session.setAttribute("registrationOtp", otp);

            model.addAttribute("email", user.getEmail());
            model.addAttribute("otp", otp);
            return "auth/verify-otp";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyRegistrationOtp(@RequestParam("otp") String submittedOtp, HttpSession session, Model model) {
        String expectedOtp = (String) session.getAttribute("registrationOtp");
        String email = (String) session.getAttribute("pendingEmail");

        if (!otpService.isValid(expectedOtp, submittedOtp)) {
            model.addAttribute("error", "Invalid OTP. Please enter the displayed code.");
            model.addAttribute("email", email);
            model.addAttribute("otp", expectedOtp);
            return "auth/verify-otp";
        }

        User user = new User();
        user.setFirstName((String) session.getAttribute("pendingFirstName"));
        user.setLastName((String) session.getAttribute("pendingLastName"));
        user.setEmail(email);
        user.setPassword((String) session.getAttribute("pendingPassword"));
        user.setRole((Role) session.getAttribute("pendingRole"));

        try {
            userService.registerUser(user);
            clearPendingRegistration(session);
            return "redirect:/auth/login?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "auth/register";
        }
    }

    @PostMapping("/resend-otp")
    public String resendRegistrationOtp(HttpSession session, Model model) {
        String email = (String) session.getAttribute("pendingEmail");
        if (email == null) {
            return "redirect:/auth/register";
        }

        String otp = otpService.generateOtp();
        session.setAttribute("registrationOtp", otp);
        model.addAttribute("email", email);
        model.addAttribute("otp", otp);
        model.addAttribute("message", "A new OTP has been generated.");
        return "auth/verify-otp";
    }

    private void clearPendingRegistration(HttpSession session) {
        session.removeAttribute("pendingFirstName");
        session.removeAttribute("pendingLastName");
        session.removeAttribute("pendingEmail");
        session.removeAttribute("pendingPassword");
        session.removeAttribute("pendingRole");
        session.removeAttribute("registrationOtp");
    }
}
