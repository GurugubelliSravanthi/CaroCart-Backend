package com.carocart.authentication.controller;

import com.carocart.authentication.service.OtpEmailService;
import com.carocart.authentication.service.UserService;
import com.carocart.authentication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private OtpEmailService otpService;

    @Autowired
    private UserService userService;

    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOtp(@RequestParam String emailOrPhone) {
        Optional<User> userOpt = userService.findByEmail(emailOrPhone); // You may later extend this for phone
        if (userOpt.isPresent()) {
            String otp = otpService.generateOtp();
            otpService.storeOtp(emailOrPhone, otp);
            otpService.sendOtpEmail(emailOrPhone, otp); // Use separate service for SMS if needed
            return ResponseEntity.ok("OTP sent to " + emailOrPhone);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String emailOrPhone, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(emailOrPhone, otp);
        return isValid
                ? ResponseEntity.ok("OTP verified")
                : ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String emailOrPhone,
                                                @RequestParam String otp,
                                                @RequestParam String newPassword) {
        if (!otpService.validateOtp(emailOrPhone, otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        boolean updated = userService.updatePassword(emailOrPhone, newPassword);
        if (updated) {
            otpService.removeOtp(emailOrPhone);
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update password");
        }
    }
}

