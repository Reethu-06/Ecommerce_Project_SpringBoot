//package com.newOne.security;
//
//import com.newOne.entity.User;
//import com.newOne.customException.EcommerceException;
//import com.newOne.repository.UserRepository;
//import com.newOne.security.JwtUtil;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.Random;
//
//@Service
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final RedisService redisService; // For OTP management
//    private final JwtUtil jwtUtil; // For JWT token generation
//
//    public AuthService(UserRepository userRepository, RedisService redisService, JwtUtil jwtUtil) {
//        this.userRepository = userRepository;
//        this.redisService = redisService;
//        this.jwtUtil = jwtUtil;
//    }
//
//    // ========================== ADMIN LOGIN ==========================
//    public String adminLogin(String email, String password) {
//        Optional<User> adminOpt = userRepository.findByEmail(email);
//        if (adminOpt.isPresent()) {
//            User admin = adminOpt.get();
//            if (admin.getRoleId() == 1 && BCrypt.checkpw(password, admin.getPasswordHash())) {
//                return jwtUtil.generateToken(admin.getId(), "ADMIN");
//            }
//        }
//        return null;
//    }
//
//    // ========================== USER OTP LOGIC ==========================
//    public String sendOtp(String phoneNumber) {
//        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
//        if (userOpt.isPresent()) {
//            String otp = String.valueOf(100000 + new Random().nextInt(900000)); // 6-digit OTP
//            redisService.storeOtp(phoneNumber, otp);
//            // Logic to send OTP via SMS can be added here
//            return "OTP Sent Successfully";
//        }
//        return "User not found";
//    }
//
//    public String verifyOtp(String phoneNumber, String otp) {
//        String storedOtp = redisService.getOtp(phoneNumber);
//        if (storedOtp != null && storedOtp.equals(otp)) {
//            User user = userRepository.findByPhoneNumber(phoneNumber)
//                    .orElseThrow(() -> new EcommerceException(HttpStatus.NOT_FOUND, "User not found."));
//            return jwtUtil.generateToken(user.getId(), "USER");
//        }
//        return null;
//    }
//}
