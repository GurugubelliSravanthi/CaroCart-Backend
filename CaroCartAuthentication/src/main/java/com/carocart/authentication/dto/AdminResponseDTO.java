package com.carocart.authentication.dto;

public class AdminResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private boolean hasProfileImage;

    // Constructors
    public AdminResponseDTO() {}

    public AdminResponseDTO(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public AdminResponseDTO(Long id, String username, String email, String role, 
                           String firstName, String lastName, boolean hasProfileImage) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hasProfileImage = hasProfileImage;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public boolean isHasProfileImage() { return hasProfileImage; }
    public void setHasProfileImage(boolean hasProfileImage) { this.hasProfileImage = hasProfileImage; }
}