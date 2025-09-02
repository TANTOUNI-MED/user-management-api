package com.example.usermanagement.controller;

import com.example.usermanagement.dto.BatchImportResponse;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "Generate users", description = "Generate a JSON file with random users")
    @ApiResponse(responseCode = "200", description = "Users generated successfully")
    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateUsers(
            @Parameter(description = "Number of users to generate") 
            @RequestParam int count,
            HttpServletResponse response) throws JsonProcessingException {
        
        List<UserDto> users = userService.generateUsers(count);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(users);
        
        // Set headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "users.json");
        
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }
    
    @Operation(summary = "Batch import users", description = "Import users from a JSON file")
    @ApiResponse(responseCode = "200", description = "Users imported successfully")
    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BatchImportResponse batchImportUsers(
            @Parameter(description = "JSON file containing users") 
            @RequestParam("file") MultipartFile file) {
        
        return userService.importUsers(file);
    }
    
    @Operation(summary = "Get my profile", description = "Get the profile of the currently authenticated user")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @GetMapping("/me")
    public User getMyProfile() {
        return userService.getCurrentUserProfile();
    }
    
    @Operation(summary = "Get user profile", description = "Get the profile of a specific user (admin only)")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{username}")
    public User getUserProfile(
            @Parameter(description = "Username of the user to retrieve") 
            @PathVariable String username) {
        
        return userService.getUserProfile(username);
    }
}