package com.app.echoboard.controller;

import com.app.echoboard.model.Role;
import com.app.echoboard.model.User;
import com.app.echoboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * User Controller - User management API
 * Provide user registration, query, etc.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allow cross-origin access
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * User registration
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, "Email already exists!"));
        }

        // Create new user
        User user = new User(request.getEmail(), request.getName(), request.getRole());
        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully!", savedUser));
    }

    /**
     * Get all users
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get user by email
     * GET /api/users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get user list by role
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        List<User> users = userRepository.findByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Get active user list
     * GET /api/users/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> users = userRepository.findByIsActiveTrue();
        return ResponseEntity.ok(users);
    }

    // DTO class: user registration request
    public static class UserRegistrationRequest {
        private String email;
        private String name;
        private Role role;

        // Constructor
        public UserRegistrationRequest() {}

        public UserRegistrationRequest(String email, String name, Role role) {
            this.email = email;
            this.name = name;
            this.role = role;
        }

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
    }

    // Common API response class
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}