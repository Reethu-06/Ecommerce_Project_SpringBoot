package com.newOne.service;

import com.newOne.request.AdminLoginRequest;
import com.newOne.request.AdminRegistrationRequest;
import com.newOne.request.UserLoginRequest;
import com.newOne.request.UserRegistrationRequest;

public interface UserService {

    /**
     * Registers a new user.
     *
     * @param request The registration details for the new user.
     * @return A success or failure message.
     */
    String registerUser(UserRegistrationRequest request);

    /**
     * Registers a new admin.
     *
     * @param request The registration details for the new admin.
     * @return A success or failure message.
     */
    String registerAdmin(AdminRegistrationRequest request);

    /**
     * Validates the login credentials for a user.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A boolean indicating whether the login credentials are valid.
     */
    boolean validateLogin(String email, String password);

    /**
     * Sends an OTP to the user's phone number.
     *
     * @param phoneNumber The phone number where the OTP will be sent.
     * @return A success or failure message.
     */
    String sendOtp(String phoneNumber);

    /**
     * Verifies the OTP entered by the user.
     *
     * @param request The request containing the phone number and OTP.
     * @return A success or failure message after verifying the OTP.
     */
    String verifyOtp(UserLoginRequest request);

    /**
     * Logs in the admin with their credentials.
     *
     * @param request The login credentials for the admin.
     * @return A success or failure message after attempting to log in.
     */
    String adminLogin(AdminLoginRequest request);
}
