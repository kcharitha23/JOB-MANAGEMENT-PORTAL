package com.jobportal.service.impl;

import com.jobportal.service.OtpService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpServiceImpl implements OtpService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String generateOtp() {
        int otp = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public boolean isValid(String expectedOtp, String submittedOtp) {
        return expectedOtp != null
                && submittedOtp != null
                && expectedOtp.equals(submittedOtp.trim());
    }
}
