package com.newOne.serviceImpl;

import com.newOne.customException.EcommerceException;
import com.newOne.entity.User;
import com.newOne.entity.Wallet;
import com.newOne.repository.UserRepository;
import com.newOne.repository.WalletRepository;
import com.newOne.request.AdminLoginRequest;
import com.newOne.request.AdminRegistrationRequest;
import com.newOne.request.UserLoginRequest;
import com.newOne.request.UserRegistrationRequest;
import com.newOne.security.JwtUtil;
import com.newOne.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;

    // Constructor to inject dependencies
    public UserServiceImpl(UserRepository userRepository, WalletRepository walletRepository,
                           PasswordEncoder passwordEncoder, StringRedisTemplate redisTemplate, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String registerUser(UserRegistrationRequest request) {
        // Log the start of user registration
        System.out.println("Registering user with email: " + request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EcommerceException(HttpStatus.CONFLICT, "Email already registered.");
        }

        // Create and save the new user
        User user = new User(
                request.getUsername(),
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                passwordEncoder.encode(request.getPassword()),
                2 // Role ID 2 for Users
        );
        userRepository.save(user);

        // Log user creation
        System.out.println("User registered: " + user.getUsername());

        // Create wallet for the new user
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0); // Initialize wallet with 0 balance
        walletRepository.save(wallet);

        // Log wallet creation
        System.out.println("Wallet created for user: " + user.getUsername());

        return "User registered successfully.";
    }

    @Override
    public String registerAdmin(AdminRegistrationRequest request) {
        // Log the start of admin registration
        System.out.println("Registering admin with email: " + request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EcommerceException(HttpStatus.CONFLICT, "Email already exists.");
        }

        // Create and save the new admin
        User admin = new User(
                request.getUsername(),
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                passwordEncoder.encode(request.getPassword()),
                1 // Role ID 1 for Admin
        );
        userRepository.save(admin);

        // Log admin creation
        System.out.println("Admin registered: " + admin.getUsername());

        return "Admin registered successfully.";
    }

    @Override
    public boolean validateLogin(String email, String password) {
        // Log the login validation process
        System.out.println("Validating login for email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EcommerceException(HttpStatus.NOT_FOUND, "User not found."));

        boolean isValid = passwordEncoder.matches(password, user.getPasswordHash());

        // Log the result of login validation
        if (isValid) {
            System.out.println("Login validated successfully for email: " + email);
        } else {
            System.out.println("Invalid credentials for email: " + email);
        }

        return isValid;
    }

    @Override
    public String sendOtp(String phoneNumber) {
        // Log the OTP sending request
        System.out.println("Sending OTP to phone number: " + phoneNumber);

        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isEmpty()) {
            throw new EcommerceException(HttpStatus.NOT_FOUND, "User not found with this phone number.");
        }

        String otp = generateOtp();
        redisTemplate.opsForValue().set(phoneNumber, otp, 5, TimeUnit.MINUTES);

        // Log the OTP sent
        System.out.println("OTP sent to phone number: " + phoneNumber + " (OTP: " + otp + ")");

        return "OTP sent successfully. OTP: " + otp; // For testing purposes, returning the OTP. Remove this in production.
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // Generates a 6-digit OTP
    }

    @Override
    public String verifyOtp(UserLoginRequest request) {
        // Log the OTP verification process
        System.out.println("Verifying OTP for phone number: " + request.getPhoneNumber());

        String storedOtp = redisTemplate.opsForValue().get(request.getPhoneNumber());

        if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
            throw new EcommerceException(HttpStatus.UNAUTHORIZED, "Invalid OTP or OTP expired.");
        }

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new EcommerceException(HttpStatus.NOT_FOUND, "User not found."));

        redisTemplate.delete(request.getPhoneNumber()); // Clear OTP after successful login

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), 2); // Role ID 2 for User

        // Log successful OTP verification and token generation
        System.out.println("OTP verified successfully for phone number: " + request.getPhoneNumber());
        System.out.println("Token generated for user: " + user.getUsername());

        return "Welcome " + user.getUsername() + "! Your token: " + token;
    }

    @Override
    public String adminLogin(AdminLoginRequest request) {
        // Log the admin login process
        System.out.println("Admin login attempt for email: " + request.getEmail());

        User admin = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EcommerceException(HttpStatus.NOT_FOUND, "Admin not found."));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
            throw new EcommerceException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        String token = jwtUtil.generateToken(admin.getEmail(), admin.getId(), 1); // Role ID 1 for Admin

        // Log successful admin login
        System.out.println("Admin login successful for email: " + request.getEmail());

        return "Login successful. Your token: " + token;
    }
}
