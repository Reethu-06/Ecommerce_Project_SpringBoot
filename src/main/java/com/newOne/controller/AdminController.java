//package com.newOne.controller;
//
//import com.newOne.customException.EcommerceException;
//import com.newOne.entity.User;
//import com.newOne.repository.UserRepository;
//import com.newOne.request.AdminLoginRequest;
//import com.newOne.request.AdminRegistrationRequest;
//import com.newOne.service.AdminService;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//@Slf4j
//@RestController
//@RequestMapping("/admin")
//public class AdminController {
//
//    private final AdminService adminService;
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//
//    public AdminController(AdminService adminService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
//        this.adminService = adminService;
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegistrationRequest request) {
//        return ResponseEntity.ok(adminService.registerAdmin(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody AdminLoginRequest request) {
//        boolean isAuthenticated = adminService.validateLogin(request.getEmail(), request.getPassword());
//
//        if (isAuthenticated) {
//            return ResponseEntity.ok("Login successful.");
//        } else {
//            return ResponseEntity.status(401).body("Invalid email or password.");
//        }
//    }
//
//}
