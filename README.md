# User Management API

A comprehensive Spring Boot REST API for user management with JWT authentication, featuring user generation, batch processing, and role-based access control.

---

## Features
- **User Generation**: Generate realistic user data with JavaFaker  
- **Batch Processing**: Import users via JSON file upload with duplicate checking  
- **JWT Authentication**: Secure authentication with JSON Web Tokens  
- **Role-Based Access Control**: Admin and user roles with appropriate permissions  
- **H2 Database**: In-memory database with web console access  
- **Swagger Documentation**: Interactive API documentation  
- **File Download**: JSON file generation and download  

---

## Technologies Used
- Java 17 with Spring Boot 3.5.5  
- Spring Security with JWT authentication  
- Spring Data JPA with H2 database  
- JavaFaker for realistic test data generation  
- SpringDoc OpenAPI for API documentation  
- Maven for dependency management  

---

## API Endpoints

### 1. User Generation

GET /api/users/generate?count={number}
  

### 2. Batch Import

POST /api/users/batch
Content-Type: multipart/form-data
  Generates and downloads a JSON file with specified number of users.
  
### 3. Authentication

POST /api/auth
Content-Type: application/json
  Authenticates users and returns JWT token. Accepts either username or email.
  

### 4. Get My Profile

GET /api/users/me
Authorization: Bearer {token}
  Returns the profile of the currently authenticated user.


### 5. Get User Profile (Admin only)

GET /api/users/{username}
Authorization: Bearer {token}
  Returns the profile of any user (admin access required).


