package com.example.usermanagement.service;

import com.example.usermanagement.dto.BatchImportResponse;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private Faker faker = new Faker();
    private Random random = new Random();
    
    @Override
    public List<UserDto> generateUsers(int count) {
        List<UserDto> users = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            UserDto userDto = new UserDto();
            
            // Generate realistic data using JavaFaker
            userDto.setFirstName(faker.name().firstName());
            userDto.setLastName(faker.name().lastName());
            
            // Generate birth date between 18 and 80 years ago
            Date birthDate = faker.date().birthday(18, 80);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            userDto.setBirthDate(formatter.format(birthDate));
            
            userDto.setCity(faker.address().city());
            userDto.setCountry(faker.address().countryCode());
            userDto.setAvatar(faker.internet().avatar());
            userDto.setCompany(faker.company().name());
            userDto.setJobPosition(faker.job().position());
            userDto.setMobile(faker.phoneNumber().cellPhone());
            
            // Generate unique username and email
            String username = faker.name().username().replaceAll("[^a-zA-Z0-9]", "");
            userDto.setUsername(username);
            userDto.setEmail(faker.internet().emailAddress(username));
            
            // Generate password between 6 and 10 characters
            userDto.setPassword(generateRandomPassword());
            
            // Random role (admin or user)
            userDto.setRole(random.nextBoolean() ? "admin" : "user");
            
            users.add(userDto);
        }
        
        return users;
    }
    
    private String generateRandomPassword() {
        int passwordLength = 6 + random.nextInt(5); // 6 to 10 characters
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        
        return password.toString();
    }
    
    @Override
    public BatchImportResponse importUsers(MultipartFile file) {
        int totalRecords = 0;
        int successfulImports = 0;
        int failedImports = 0;
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserDto[] userDtos = objectMapper.readValue(file.getInputStream(), UserDto[].class);
            totalRecords = userDtos.length;
            
            for (UserDto userDto : userDtos) {
                try {
                    // Check for duplicates
                    if (userRepository.existsByUsername(userDto.getUsername()) || 
                        userRepository.existsByEmail(userDto.getEmail())) {
                        failedImports++;
                        continue;
                    }
                    
                    // Create user entity
                    User user = new User(
                        userDto.getFirstName(),
                        userDto.getLastName(),
                        userDto.getBirthDate(),
                        userDto.getCity(),
                        userDto.getCountry(),
                        userDto.getAvatar(),
                        userDto.getCompany(),
                        userDto.getJobPosition(),
                        userDto.getMobile(),
                        userDto.getUsername(),
                        userDto.getEmail(),
                        passwordEncoder.encode(userDto.getPassword()),
                        userDto.getRole()
                    );
                    
                    userRepository.save(user);
                    successfulImports++;
                } catch (Exception e) {
                    failedImports++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON file", e);
        }
        
        return new BatchImportResponse(totalRecords, successfulImports, failedImports);
    }
    
    @Override
    public User findByUsernameOrEmail(String identifier) {
        // First try to find by username
        Optional<User> byUsername = userRepository.findByUsername(identifier);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        
        // Then try to find by email
        Optional<User> byEmail = userRepository.findByEmail(identifier);
        if (byEmail.isPresent()) {
            return byEmail.get();
        }
        
        // If neither found, return null
        return null;
    }
    
    @Override
    public User getCurrentUserProfile() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElse(null);
    }
    
    @Override
    public User getUserProfile(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}