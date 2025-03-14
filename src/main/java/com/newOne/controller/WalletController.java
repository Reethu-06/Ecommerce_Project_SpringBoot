package com.newOne.controller;

import com.newOne.entity.Wallet;
import com.newOne.request.WalletTopupRequest;
import com.newOne.security.RoleAccessUtil;
import com.newOne.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * WalletController handles wallet-related operations like top-up, retrieval, and deletion.
 */
@RestController
@RequestMapping("/wallet")
@Slf4j  // Lombok annotation to generate logger
public class WalletController {

    private final WalletService walletService;
    private final RoleAccessUtil roleAccessUtil;

    public WalletController(WalletService walletService, RoleAccessUtil roleAccessUtil) {
        this.walletService = walletService;
        this.roleAccessUtil = roleAccessUtil;
    }

    /**
     * Top-up the user's wallet. Only accessible to admin users.
     *
     * @param request The wallet top-up request containing the amount and user details.
     * @param httpRequest The HTTP request object for access control.
     * @return A response with the result of the wallet top-up operation.
     */
    @PostMapping("/topup")
    public ResponseEntity<String> topUpWallet(@RequestBody WalletTopupRequest request, HttpServletRequest httpRequest) {
        log.info("Attempting to top-up wallet for user ID: {}", request.getUserId());

        // Check if the user has admin access before proceeding
        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied for wallet top-up for user ID: {}", request.getUserId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        // Call the service to top-up the wallet
        String response = walletService.topUpWallet(request);
        log.info("Successfully topped-up wallet for user ID: {}", request.getUserId());

        return ResponseEntity.ok(response);
    }

    /**
     * Get the wallet details of a user. Accessible to both admin and user roles.
     *
     * @param userId The user ID whose wallet details are to be retrieved.
     * @param httpRequest The HTTP request object for access control.
     * @return The wallet details for the user.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getWalletDetails(@PathVariable int userId, HttpServletRequest httpRequest) {
        log.info("Fetching wallet details for user ID: {}", userId);

        // Check if the user has either admin or user access
        if (!roleAccessUtil.hasAdminAccess(httpRequest) && !roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied for retrieving wallet details for user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        // Call the service to get wallet details
        Wallet wallet = walletService.getWalletDetails(userId);
        log.info("Successfully fetched wallet details for user ID: {}", userId);

        return ResponseEntity.ok(wallet);
    }

    /**
     * Delete the wallet of a user. Only accessible to admin users.
     *
     * @param userId The user ID whose wallet is to be deleted.
     * @param httpRequest The HTTP request object for access control.
     * @return A response indicating whether the wallet was deleted successfully.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteWallet(@PathVariable int userId, HttpServletRequest httpRequest) {
        log.info("Attempting to delete wallet for user ID: {}", userId);

        // Check if the user has admin access before proceeding
        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied for wallet deletion for user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        // Call the service to delete the wallet
        String response = walletService.deleteWallet(userId);
        log.info("Successfully deleted wallet for user ID: {}", userId);

        return ResponseEntity.ok(response);
    }
}
