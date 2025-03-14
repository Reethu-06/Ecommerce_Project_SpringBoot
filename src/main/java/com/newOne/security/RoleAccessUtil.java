package com.newOne.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RoleAccessUtil {

    /**
     * Checks if the user has access based on the role attribute in the request.
     * This is specifically for users with the role ID "2" (USER).
     *
     * @param httpRequest the incoming HTTP request
     * @return true if the user has the role "USER", false otherwise
     */
    public boolean hasUserAccess(HttpServletRequest httpRequest) {
        // Check if the role attribute in the request is "2" (USER role)
        return "2".equals(httpRequest.getAttribute("role")); // Role ID for USER
    }

    /**
     * Checks if the user has admin access based on the role attribute in the request.
     * This is specifically for users with the role ID "1" (ADMIN).
     *
     * @param httpRequest the incoming HTTP request
     * @return true if the user has the role "ADMIN", false otherwise
     */
    public boolean hasAdminAccess(HttpServletRequest httpRequest) {
        // Check if the role attribute in the request is "1" (ADMIN role)
        return "1".equals(httpRequest.getAttribute("role")); // Role ID for ADMIN
    }

    /**
     * Checks if the user has access for either user or admin roles.
     * This method returns true if the role is either "1" (ADMIN) or "2" (USER).
     *
     * @param httpRequest the incoming HTTP request
     * @return true if the user has either the "USER" or "ADMIN" role, false otherwise
     */
    public boolean hasAccessForBoth(HttpServletRequest httpRequest) {
        // Retrieve the role attribute from the request
        String role = (String) httpRequest.getAttribute("role");
        // Check if the role is either "1" (ADMIN) or "2" (USER)
        return "1".equals(role) || "2".equals(role);  // Either USER or ADMIN
    }
}
