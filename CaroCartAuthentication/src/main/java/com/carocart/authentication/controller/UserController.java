package com.carocart.authentication.controller;

import com.carocart.authentication.dto.UserDTO;
import com.carocart.authentication.entity.User;
import com.carocart.authentication.repository.UserRepository;
import com.carocart.authentication.service.OtpEmailService;
import com.carocart.authentication.service.UserService;
import com.carocart.authentication.util.JwtUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OtpEmailService otpEmailService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        String result = userService.signup(user);
        if ("User registered successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            String token = userService.login(user.getEmail(), user.getPassword());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestHeader("Authorization") String authorizationHeader,
                                                @RequestBody User updatedUser) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }
        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        boolean success = userService.updateUserProfile(email, updatedUser);
        if (success) {
            return ResponseEntity.ok("Profile updated successfully");
        }
        return ResponseEntity.badRequest().body("Update failed");
    }

    @GetMapping("/admin/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
//    
    @PutMapping("/admin/users/{id}")
    public ResponseEntity<String> updateUserByAdmin(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());
        userRepository.save(user);

        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
//

    // ✅ New endpoint to support CartService Feign call
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getRole());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    

    // ✅ Step 1: Request OTP for password reset
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (!userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email not registered");
        }
        
        String otp = otpEmailService.generateOtp();
        otpEmailService.storeOtp(email, otp);
        otpEmailService.sendOtpEmail(email, otp);
        
        return ResponseEntity.ok("OTP sent to email");
    }

    // ✅ Step 2: Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = otpEmailService.validateOtp(email, otp);
        if (isValid) {
            otpEmailService.removeOtp(email); // Clear OTP after verification
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }

    // ✅ Step 3: Reset password after OTP verification
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword"); {
        boolean success = userService.updatePassword(email, newPassword);
        return success ?
                ResponseEntity.ok("Password updated successfully") :
                ResponseEntity.badRequest().body("Failed to update password");
    }
    
   }
    @PostMapping("/profile/upload-image")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("profileImage") MultipartFile file, // Changed to match frontend
            @RequestHeader("Authorization") String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            // Check file size (e.g., limit to 2MB)
            long maxSize = 2 * 1024 * 1024; // 2MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest()
                    .body("File size exceeds the 2MB limit");
            }

            // Check content type
            String contentType = file.getContentType();
            if (contentType == null || 
                !(contentType.equals("image/jpeg") || 
                 contentType.equals("image/png") ||
                 contentType.equals("image/gif"))) {
                return ResponseEntity.badRequest()
                    .body("Only JPEG, PNG, or GIF images are allowed");
            }

            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();
            user.setProfileImage(file.getBytes());
            userRepository.save(user);
            
            return ResponseEntity.ok("Profile image updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to process image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/profile/image")
    public ResponseEntity<byte[]> getProfileImage(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty() || userOpt.get().getProfileImage() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] image = userOpt.get().getProfileImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Or detect type dynamically
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
    
    

}
