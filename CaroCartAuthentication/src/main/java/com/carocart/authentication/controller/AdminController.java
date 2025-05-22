package com.carocart.authentication.controller;

import com.carocart.authentication.dto.AdminResponseDTO;
import com.carocart.authentication.entity.Admin;
import com.carocart.authentication.repository.AdminRepository;
import com.carocart.authentication.service.AdminService;
import com.carocart.authentication.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Admin admin) {
        String result = adminService.signup(admin);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        String token = adminService.login(email, password);

        if (token.equals("Admin not found") || token.equals("Invalid credentials")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", token));
        }

        return ResponseEntity.ok(Map.of("token", token, "role", "ADMIN"));
    }

    @GetMapping("/me")
    public ResponseEntity<AdminResponseDTO> getCurrentAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = auth.getName();  // This is the email set as principal in JwtAuthenticationFilter

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        AdminResponseDTO dto = new AdminResponseDTO(
                admin.getId(),
                admin.getUsername(),
                admin.getEmail(),
                admin.getRole()
        );

        return ResponseEntity.ok(dto);
    }

}
