package com.carocart.authentication.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carocart.authentication.entity.Vendor;
import com.carocart.authentication.repository.VendorRepository;
import com.carocart.authentication.util.JwtUtil;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private OtpEmailService otpEmailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private final Map<String, Vendor> pendingVendors = new ConcurrentHashMap<>();

    // Step 1: Request OTP for signup
    public String requestSignupOtp(Vendor vendor) {
        if (vendorRepository.existsByEmail(vendor.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String otp = otpEmailService.generateOtp();
        otpEmailService.storeOtp(vendor.getEmail(), otp);
        pendingVendors.put(vendor.getEmail(), vendor);
        otpEmailService.sendOtpEmail(vendor.getEmail(), otp);

        return "OTP sent to your email";
    }

    // Step 2: Verify OTP and complete registration
    public String verifySignupOtp(String email, String otp) {
        if (!otpEmailService.validateOtp(email, otp)) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        Vendor vendor = pendingVendors.get(email);
        if (vendor == null) {
            throw new IllegalStateException("No pending signup found for this email");
        }

        vendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
        vendor.setApproved(false);
        vendorRepository.save(vendor);

        otpEmailService.removeOtp(email);
        pendingVendors.remove(email);

        return "Vendor registered successfully, pending admin approval";
    }

    // Step 3: Vendor Login with JWT token (only if approved)
    public String loginVendor(String email, String password) {
        Optional<Vendor> vendorOpt = vendorRepository.findByEmail(email);
        if (vendorOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Vendor vendor = vendorOpt.get();

        if (!passwordEncoder.matches(password, vendor.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!vendor.isApproved()) {
            throw new IllegalStateException("Vendor not approved yet");
        }

        return jwtUtil.generateToken(
                vendor.getEmail(),
                "VENDOR", // or user.getRole() if dynamic
                vendor.getFirstName(),
                vendor.getLastName()
        );
    }

    // Step 4: Admin approval of vendor
    public boolean approveVendor(Long vendorId) {
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isPresent()) {
            Vendor vendor = optionalVendor.get();
            vendor.setApproved(true);
            vendorRepository.save(vendor);
            otpEmailService.sendApprovalEmail(vendor.getEmail());
            return true;
        }
        return false;
    }

    // Utility method: get vendor by ID
    public Optional<Vendor> getVendorById(Long id) {
        return vendorRepository.findById(id);
    }
    
    public List<Vendor> getPendingVendors() {
        return vendorRepository.findByIsApprovedFalse();
    }
    
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }


}
