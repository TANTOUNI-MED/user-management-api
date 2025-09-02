package com.example.usermanagement.dto;

public class AuthResponse {
    private String accessToken;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    
    // Getters and Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}