package com.jobportal.controller;

import com.jobportal.util.ChatResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @PostMapping
    public ChatResponse getResponse(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message").toLowerCase();

        if (userMessage.contains("hello") || userMessage.contains("hi") || userMessage.contains("hey")) {
            return ChatResponse.builder()
                    .message("Hi! I'm CareerBuddy, your AI career assistant. Are you looking for a job or looking to hire?")
                    .suggestions(Arrays.asList("I'm looking for a job", "I want to hire someone"))
                    .build();
        } 
        
        // Employer Flow
        else if (userMessage.contains("hire") || userMessage.contains("employer") || userMessage.contains("post a job")) {
            return ChatResponse.builder()
                    .message("Great! We'd love to help you find top talent. You can post a job after registering as an employer.")
                    .suggestions(Arrays.asList("Register as Employer", "Post a Job", "Employer Dashboard"))
                    .build();
        }
        
        // Job Seeker Flow
        else if (userMessage.contains("find") || userMessage.contains("search") || userMessage.contains("job") || userMessage.contains("seeker")) {
            return ChatResponse.builder()
                    .message("I can help with that! You can browse hundreds of jobs on our platform. Do you have a specific role or location in mind?")
                    .suggestions(Arrays.asList("Browse All Jobs", "Remote Jobs", "Software Jobs"))
                    .build();
        }
        
        // Account/Registration Flow
        else if (userMessage.contains("register") || userMessage.contains("account") || userMessage.contains("signup")) {
            return ChatResponse.builder()
                    .message("To get started, you'll need an account. Which type of account do you need?")
                    .suggestions(Arrays.asList("Student Account", "Employer Account"))
                    .build();
        }
        
        // Help/Support Flow
        else if (userMessage.contains("help") || userMessage.contains("how it works") || userMessage.contains("support")) {
            return ChatResponse.builder()
                    .message("CareerFlow connects job seekers with employers. Seekers can apply for jobs, and employers can manage applications. How can I assist further?")
                    .suggestions(Arrays.asList("Find Jobs", "Employer Info", "Contact Support"))
                    .build();
        }

        return ChatResponse.builder()
                .message("I'm not quite sure about that yet, but I'm here to help you with jobs, hiring, or account setup!")
                .suggestions(Arrays.asList("Browse Jobs", "Post a Job", "Account Help"))
                .build();
    }
}
