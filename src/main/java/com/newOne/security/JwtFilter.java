package com.newOne.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * The filter method that checks the incoming request for a valid JWT token.
     * This method is invoked once per request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // ✅ Skip JWT validation for public and internal Spring error endpoints
        String path = request.getRequestURI();
        // Exclude certain paths from JWT validation (login, OTP, registration, etc.)
        if (path.startsWith("/auth/admin/login") ||
                path.startsWith("/auth/user/send-otp") ||
                path.startsWith("/auth/user/verify-otp") ||
                path.startsWith("/auth/register-user") ||
                path.startsWith("/auth/register-admin") ||
                path.startsWith("/error")) {
            filterChain.doFilter(request, response);  // Continue the request chain if path is excluded
            return;
        }

        // Extract the Authorization header (Bearer token) from the incoming request
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // Continue the request chain if no Bearer token is found
            return;
        }

        String token = authHeader.substring(7);

        try {
            // Extract username from the token
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // If the token is valid and no authentication is currently set in SecurityContext
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // Load user details from DB

                // Validate the token (check if it matches the username and is not expired)
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    // Create an authentication token for the user
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // Set additional request details
                    SecurityContextHolder.getContext().setAuthentication(authToken);  // Set the authentication in the security context

                    // ✅ Corrected role and userId handling: Adding user role and ID to request attributes
                    request.setAttribute("role", jwtUtil.extractRole(token).toString());
                    request.setAttribute("userId", jwtUtil.extractUserId(token).toString());
                }
            }
        } catch (ExpiredJwtException ex) {
            // If the token has expired, return unauthorized response with appropriate message
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired.");
            return;
        } catch (Exception e) {
            // If any other exception occurs, return unauthorized response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token.");
            return;
        }

        // Proceed with the filter chain after processing the JWT token
        filterChain.doFilter(request, response);
    }
}
