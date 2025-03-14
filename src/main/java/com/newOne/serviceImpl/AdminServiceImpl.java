//package com.newOne.serviceImpl;
//
//import com.newOne.customException.EcommerceException;
//import com.newOne.request.AdminRegistrationRequest;
//import com.newOne.entity.User;
//import com.newOne.repository.UserRepository;
//import com.newOne.service.AdminService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Slf4j
//@Service
//public class AdminServiceImpl implements AdminService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public String registerAdmin(AdminRegistrationRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new EcommerceException(HttpStatus.NOT_FOUND, "Email already exists.");
//        }
//
//        User admin = new User(
//                request.getUsername(),
//                request.getFullName(),
//                request.getEmail(),
//                request.getPhoneNumber(),
//                passwordEncoder.encode(request.getPassword()), // Hash the password
//                1 // Role ID 1 for Admin
//        );
//
//
//        userRepository.save(admin);
//        return "Registered successfully.";
//    }
//    @Override
//    public boolean validateLogin(String email, String password) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//
//        if (optionalUser.isEmpty()) {
//            log.error("Login failed: User with email {} not found", email);
//            return false;
//        }
//
//        User user = optionalUser.get();
//
//        // Check if password matches
//        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
//            log.error("Login failed: Incorrect password for email {}", email);
//            return false;
//        }
//
//        log.info("Login successful for email: {}", email);
//        return true;
//    }
//}
