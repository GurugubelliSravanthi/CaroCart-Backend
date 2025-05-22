package com.carocart.authentication.controller;

import com.carocart.authentication.entity.Vendor;
import com.carocart.authentication.service.VendorService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/signup/request-otp")
    public ResponseEntity<String> requestOtp(@RequestBody Vendor vendor) {
        try {
            String response = vendorService.requestSignupOtp(vendor);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/signup/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            String response = vendorService.verifySignupOtp(email, otp);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            String jwtToken = vendorService.loginVendor(email, password);
            return ResponseEntity.ok(jwtToken);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    @GetMapping("/admin/vendors/all")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> allVendors = vendorService.getAllVendors();
        return ResponseEntity.ok(allVendors);
    }


    @PostMapping("/admin/approve/{vendorId}")
    public ResponseEntity<String> approveVendor(@PathVariable Long vendorId) {
        boolean approved = vendorService.approveVendor(vendorId);
        if (approved) {
            return ResponseEntity.ok("Vendor approved and notified");
        } else {
            return ResponseEntity.badRequest().body("Vendor not found");
        }
    }
    
    @GetMapping("/admin/vendors/pending")
    public ResponseEntity<List<Vendor>> getPendingVendors() {
        List<Vendor> pendingVendors = vendorService.getPendingVendors();
        return ResponseEntity.ok(pendingVendors);
    }

}
