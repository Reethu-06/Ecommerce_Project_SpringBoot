package com.newOne.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class
SecurityConfig {

    private final JwtFilter jwtFilter;  // JwtFilter is used to filter incoming requests and validate JWT tokens
    private final CustomUserDetailService customUserDetailsService;  // Custom service to load user details during authentication

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailService customUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Configures the HTTP security (request handling, session management, etc.)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disables CSRF as we are using stateless authentication with JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Disables session tracking, making the app stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(  // Public endpoints that don't require authentication
                                "/auth/admin/login",
                                "/auth/user/send-otp",
                                "/auth/user/verify-otp",
                                "/auth/user/register",
                                "/auth/register",
                                "/auth/admin/register",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/error"  // ✅ Added to bypass error path issues
                        ).permitAll()  // Permit all the listed URLs without authentication
                        .anyRequest().authenticated()  // All other requests need authentication
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Adds the JWT filter before UsernamePasswordAuthenticationFilter

        return http.build();
    }

    // Configures the AuthenticationProvider used for authentication
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);  // Uses custom user details service
        authProvider.setPasswordEncoder(passwordEncoder());  // Sets the password encoder (BCrypt)
        return authProvider;
    }

    // Configures the AuthenticationManager used to manage authentication processes
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configures the password encoder to be used for password validation and encryption
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Uses BCrypt algorithm to encode passwords
    }
}
