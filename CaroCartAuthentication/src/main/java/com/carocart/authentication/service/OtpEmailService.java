package com.carocart.authentication.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpEmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Store OTPs temporarily: email -> OTP
    private Map<String, String> otpStorage = new ConcurrentHashMap<>();

    // Generate 6-digit OTP
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Store OTP for an email
    public void storeOtp(String email, String otp) {
        otpStorage.put(email, otp);
    }

    // Validate OTP for an email
    public boolean validateOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }

    // Remove OTP after verification or expiration
    public void removeOtp(String email) {
        otpStorage.remove(email);
    }

    // Send OTP email
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify Your Email for CaroCart Vendor Signup");
        message.setText("Dear Vendor,\n\n"
                + "Your One-Time Password (OTP) for CaroCart signup is: " + otp + "\n\n"
                + "This OTP is valid for 5 minutes. Please do not share this OTP with anyone.\n\n"
                + "Thank you,\nCaroCart Team");
        mailSender.send(message);
    }

    // Send approval email
    public void sendApprovalEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Vendor Account is Approved");
        message.setText("Dear Vendor,\n\n"
                + "Congratulations! Your vendor account has been approved by CaroCart admin.\n\n"
                + "You can now log in and start using your account.\n\n"
                + "Thank you,\nCaroCart Team");
        mailSender.send(message);
    }
}
