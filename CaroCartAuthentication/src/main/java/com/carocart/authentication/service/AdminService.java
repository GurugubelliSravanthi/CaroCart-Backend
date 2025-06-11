package com.carocart.authentication.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.carocart.authentication.entity.Admin;
import com.carocart.authentication.repository.AdminRepository;
import com.carocart.authentication.util.JwtUtil;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    public String signup(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            return "Admin already exists";
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ADMIN"); // Set role explicitly
        adminRepository.save(admin);
        return "Admin registered successfully";
    }

    public String login(String email, String password) {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isEmpty()) {
            return "Admin not found";
        }
        Admin admin = optionalAdmin.get();
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return "Invalid credentials";
        }
        return jwtUtil.generateToken(
                admin.getEmail(),
                "ADMIN",
                admin.getFirstName(),
                admin.getLastName()
        );
    }
    
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    
    // Updated method to return Optional<Admin>
    public Admin findAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }
    public void save(Admin admin) {
        adminRepository.save(admin);
    }

}