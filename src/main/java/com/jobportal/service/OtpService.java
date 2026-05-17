package com.jobportal.service;

public interface OtpService {
    String generateOtp();
    boolean isValid(String expectedOtp, String submittedOtp);
}
