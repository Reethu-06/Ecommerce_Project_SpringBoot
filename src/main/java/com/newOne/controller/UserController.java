package com.newOne.controller;

import com.newOne.request.AdminRegistrationRequest;
import com.newOne.request.UserRegistrationRequest;
import com.newOne.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController handles user-related endpoints such as registration for both regular users and admins.
 */
@RestController
@RequestMapping("/auth")
@Slf4j  // Lombok annotation to generate logger
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param request The user registration request containing user details.
     * @return A response indicating success or failure.
     */
    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Call the service to register the user and return the result
        String response = userService.registerUser(request);
        log.info("User registration successful for email: {}", request.getEmail());

        return ResponseEntity.ok(response);
    }

    /**
     * Registers a new admin.
     *
     * @param request The admin registration request containing admin details.
     * @return A response indicating success or failure.
     */
    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegistrationRequest request) {
        log.info("Registering new admin with email: {}", request.getEmail());

        // Call the service to register the admin and return the result
        String response = userService.registerAdmin(request);
        log.info("Admin registration successful for email: {}", request.getEmail());

        return ResponseEntity.ok(response);
    }
}
