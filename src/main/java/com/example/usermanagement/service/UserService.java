package com.example.usermanagement.service;


import org.springframework.web.multipart.MultipartFile;

import com.example.usermanagement.dto.BatchImportResponse;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> generateUsers(int count);
    BatchImportResponse importUsers(MultipartFile file);
    User findByUsernameOrEmail(String identifier);
    User getCurrentUserProfile();
    User getUserProfile(String username);
}