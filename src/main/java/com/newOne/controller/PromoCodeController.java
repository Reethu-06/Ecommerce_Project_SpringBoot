package com.newOne.controller;

import com.newOne.entity.PromoCode;
import com.newOne.promo.PromoStatus;
import com.newOne.repository.PromoCodeRepository;
import com.newOne.request.PromoCodeRequest;
import com.newOne.response.PromoCodeResponse;
import com.newOne.security.RoleAccessUtil;
import com.newOne.service.PromoCodeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;  // Importing the Lombok @Slf4j annotation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller to handle all PromoCode-related requests.
 * Provides methods to manage and retrieve promo codes for the users and admins.
 */
@RestController
@RequestMapping("/promo")
@Slf4j
public class PromoCodeController {

    @Autowired
    private PromoCodeService promoCodeService;

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    private RoleAccessUtil roleAccessUtil;

    /**
     * Create a new promo code.
     * Only accessible by admin users.
     *
     * @param request  The promo code request body containing necessary data
     * @param httpRequest  The HTTP request for user information (for access control)
     * @return  A response indicating whether the promo code creation was successful or not
     */
    @PostMapping("/add")
    public ResponseEntity<String> createPromoCode(@RequestBody PromoCodeRequest request, HttpServletRequest httpRequest) {
        log.info("Attempting to create promo code with request: {}", request);

        // Check if the user has admin access
        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied for user {} while creating promo code.", httpRequest.getRemoteUser());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        try {
            // Attempt to create the promo code and return the result
            String result = promoCodeService.createPromoCode(request);
            log.info("Promo code created successfully: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Log the error and return a bad request response
            log.error("Error creating promo code: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Fetch valid product-level promo codes.
     * Accessible by both admin and user roles.
     *
     * @param httpRequest  The HTTP request for user information (for access control)
     * @return  A list of valid product promo codes
     */
    @GetMapping("/product")
    public ResponseEntity<List<PromoCodeResponse>> getProductPromoCodes(HttpServletRequest httpRequest) {
        log.info("Fetching product promo codes for user: {}", httpRequest.getRemoteUser());

        // Check if the user has valid access (admin or user)
        if (!roleAccessUtil.hasAccessForBoth(httpRequest)) {
            log.warn("Access denied for user {} while fetching product promo codes.", httpRequest.getRemoteUser());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Fetch and return the list of valid product promo codes
        List<PromoCodeResponse> promoCodes = promoCodeService.getValidProductPromoCodes();
        log.info("Fetched {} product promo codes.", promoCodes.size());
        return ResponseEntity.ok(promoCodes);
    }

    /**
     * Fetch valid order-level promo codes.
     * Accessible by both admin and user roles.
     *
     * @param httpRequest  The HTTP request for user information (for access control)
     * @return  A list of valid order promo codes
     */
    @GetMapping("/order")
    public ResponseEntity<List<PromoCodeResponse>> getOrderPromoCodes(HttpServletRequest httpRequest) {
        log.info("Fetching order promo codes for user: {}", httpRequest.getRemoteUser());

        // Check if the user has valid access (admin or user)
        if (!roleAccessUtil.hasAccessForBoth(httpRequest)) {
            log.warn("Access denied for user {} while fetching order promo codes.", httpRequest.getRemoteUser());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Fetch and return the list of valid order promo codes
        List<PromoCodeResponse> promoCodes = promoCodeService.getValidOrderPromoCodes();
        log.info("Fetched {} order promo codes.", promoCodes.size());
        return ResponseEntity.ok(promoCodes);
    }

    /**
     * Fetch all promo codes.
     * Only accessible by regular users.
     *
     * @param httpRequest  The HTTP request for user information (for access control)
     * @return  A list of all promo codes or an empty response if none are found
     */
    @GetMapping("/all")
    public ResponseEntity<List<PromoCodeResponse>> getAllPromoCodes(HttpServletRequest httpRequest) {
        log.info("Fetching all promo codes for user: {}", httpRequest.getRemoteUser());

        // Check if the user has regular user access
        if (!roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied for user {} while fetching all promo codes.", httpRequest.getRemoteUser());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Fetch all promo codes and return an appropriate response
        List<PromoCodeResponse> promos = promoCodeService.getAllPromoCodes();
        if (promos.isEmpty()) {
            log.info("No promo codes available.");
            return ResponseEntity.noContent().build();
        }

        log.info("Fetched {} promo codes.", promos.size());
        return ResponseEntity.ok(promos);
    }

    /**
     * Inactivate a promo code.
     * Only accessible by admin users.
     *
     * @param promoCodeId  The ID of the promo code to inactivate
     * @param httpRequest  The HTTP request for user information (for access control)
     * @return  A response indicating whether the promo code was successfully inactivated
     */
    @PutMapping("/inactivate/{promoCodeId}")
    public ResponseEntity<?> inactivatePromoCode(@PathVariable int promoCodeId, HttpServletRequest httpRequest) {
        log.info("Attempting to inactivate promo code with ID: {}", promoCodeId);

        // Check if the user has admin access
        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied for user {} while inactivating promo code ID: {}.", httpRequest.getRemoteUser(), promoCodeId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        // Fetch the promo code and update its status to 'inactivated by admin'
        PromoCode promoCode = promoCodeRepository.findById(promoCodeId)
                .orElseThrow(() -> {
                    log.error("Promo code with ID {} not found.", promoCodeId);
                    return new RuntimeException("Promo code not found");
                });
        promoCode.setStatus(PromoStatus.INACTIVATED_BY_ADMIN);
        promoCodeRepository.save(promoCode);

        log.info("Promo code with ID {} inactivated successfully.", promoCodeId);
        return ResponseEntity.ok("Promo code inactivated successfully.");
    }
}
