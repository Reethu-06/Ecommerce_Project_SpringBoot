package com.newOne.security;

import com.newOne.request.AdminLoginRequest;
import com.newOne.request.UserLoginRequest;
import com.newOne.service.UserService;
import com.newOne.customException.EcommerceException;  // Importing EcommerceException
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication-related endpoints.
 * This controller handles actions like admin login, OTP sending, and OTP verification for users.
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    // Constructor to inject the UserService dependency
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Admin login endpoint.
     * This method processes the admin login request with email and password.
     *
     * @param request The admin login request containing email and password.
     * @return ResponseEntity with a message indicating successful login or failure.
     */
    @PostMapping("/admin/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminLoginRequest request) {
        log.info("Received admin login request for email: {}", request.getEmail());  // Logging incoming request

        // Process the admin login via UserService
        try {
            String responseMessage = userService.adminLogin(request);
            log.info("Admin login successful for email: {}", request.getEmail());  // Log successful login
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("Admin login failed for email: {}", request.getEmail(), e);  // Log any errors during login

            // Wrap exception into EcommerceException if it's not one already
            throw new EcommerceException("Admin login failed. Please try again later.");
        }
    }

    /**
     * Send OTP to the user's phone number.
     * This method triggers OTP sending to the provided phone number for user login verification.
     *
     * @param phoneNumber The phone number to which OTP needs to be sent.
     * @return ResponseEntity with a message indicating whether the OTP was sent successfully.
     */
    @PostMapping("/user/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String phoneNumber) {
        log.info("Received request to send OTP to phone number: {}", phoneNumber);  // Logging incoming request

        try {
            String responseMessage = userService.sendOtp(phoneNumber);
            log.info("OTP sent successfully to phone number: {}", phoneNumber);  // Log successful OTP sending
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("Failed to send OTP to phone number: {}", phoneNumber, e);  // Log any errors during OTP sending

            // Wrap exception into EcommerceException if it's not one already
            throw new EcommerceException("Failed to send OTP. Please try again later.");
        }
    }

    /**
     * Verify the OTP entered by the user.
     * This method verifies the OTP that was sent to the user's phone number.
     *
     * @param request The user login request containing the phone number and OTP.
     * @return ResponseEntity with a message indicating whether OTP verification was successful.
     */
    @PostMapping("/user/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody UserLoginRequest request) {
        log.info("Received OTP verification request for phone number: {}", request.getPhoneNumber());  // Logging incoming request

        try {
            String responseMessage = userService.verifyOtp(request);
            log.info("OTP verification successful for phone number: {}", request.getPhoneNumber());  // Log successful OTP verification
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("OTP verification failed for phone number: {}", request.getPhoneNumber(), e);  // Log any errors during OTP verification

            // Wrap exception into EcommerceException if it's not one already
            throw new EcommerceException("OTP verification failed. Please try again.");
        }
    }
}
