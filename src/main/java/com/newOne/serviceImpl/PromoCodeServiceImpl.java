package com.newOne.serviceImpl;

import com.newOne.customException.EcommerceException;
import com.newOne.entity.Cart;
import com.newOne.entity.Product;
import com.newOne.entity.PromoCode;
import com.newOne.entity.PromoType;
import com.newOne.promo.PromoStatus;
import com.newOne.repository.ProductRepository;
import com.newOne.repository.PromoCodeRepository;
import com.newOne.request.PromoCodeRequest;
import com.newOne.response.PromoCodeResponse;
import com.newOne.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service to manage Promo Code related operations
@Service
public class PromoCodeServiceImpl implements PromoCodeService {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    private ProductRepository productRepository;

    // Method to create a new promo code
    @Override
    public String createPromoCode(PromoCodeRequest request) {
        // Log input parameters
        System.out.println("Creating promo code with code: " + request.getCode());

        // Check if the promo code already exists
        if (promoCodeRepository.existsByCode(request.getCode())) {
            throw new EcommerceException("Promo code already exists!");
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(request.getCode());

        try {
            // Set promo type based on request
            promoCode.setPromoType(PromoType.valueOf(request.getPromoType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            // Log error if promo type is invalid
            System.out.println("Error: Invalid promo type provided: " + request.getPromoType());
            throw new EcommerceException("Invalid promo type. Use 'PRODUCT' or 'ORDER'.");
        }

        // Handle PRODUCT type promo codes
        if (promoCode.getPromoType() == PromoType.PRODUCT) {
            if (request.getProductId() == null) {
                throw new EcommerceException("Product ID is required for PRODUCT type promo codes.");
            }
            Optional<Product> productOpt = productRepository.findById(request.getProductId());
            if (productOpt.isEmpty()) {
                throw new EcommerceException("Product not found!");
            }
            promoCode.setProduct(productOpt.get());
        } else if (promoCode.getPromoType() == PromoType.ORDER) {
            // Handle ORDER type promo codes
            if (request.getMinOrderAmount() == null) {
                throw new EcommerceException("Min Order Amount is required for ORDER type promo codes.");
            }
            promoCode.setMinOrderAmount(request.getMinOrderAmount());
        }

        // Set discount percentage and validity dates
        promoCode.setDiscountPercentage(request.getDiscountPercentage());
        promoCode.setValidFrom(request.getValidFrom().toLocalDateTime());
        promoCode.setValidTo(request.getValidTo().toLocalDateTime());

        // Set status as ACTIVE by default
        promoCode.setStatus(PromoStatus.ACTIVE);

        // Save the promo code in the repository
        promoCodeRepository.save(promoCode);
        // Log success message
        System.out.println("Promo code created successfully: " + request.getCode());
        return "Promo code created successfully!";
    }

    // Method to fetch valid product promo codes
    @Override
    public List<PromoCodeResponse> getValidProductPromoCodes() {
        // Log the action
        System.out.println("Fetching valid product promo codes.");

        List<PromoCode> promos = promoCodeRepository.findAll().stream()
                .filter(promo -> promo.getPromoType().name().equalsIgnoreCase("PRODUCT"))
                .collect(Collectors.toList());

        if (promos.isEmpty()) {
            // Log message if no valid product promo codes found
            System.out.println("No valid product promo codes available.");
            throw new EcommerceException("No valid product promo codes available.");
        }

        // Map promo codes to response DTOs
        return promos.stream()
                .map(promo -> new PromoCodeResponse(
                        promo.getCode(),
                        promo.getPromoType().name(),
                        promo.getDiscountPercentage(),
                        promo.getProduct() != null ? promo.getProduct().getId() : null,
                        null, // minOrderAmount is not applicable for product promos
                        Timestamp.valueOf(promo.getValidFrom()),
                        Timestamp.valueOf(promo.getValidTo())
                ))
                .collect(Collectors.toList());
    }

    // Method to fetch valid order promo codes
    @Override
    public List<PromoCodeResponse> getValidOrderPromoCodes() {
        // Log the action
        System.out.println("Fetching valid order promo codes.");

        List<PromoCode> promos = promoCodeRepository.findAll().stream()
                .filter(promo -> promo.getPromoType().name().equalsIgnoreCase("ORDER"))
                .collect(Collectors.toList());

        if (promos.isEmpty()) {
            // Log message if no valid order promo codes found
            System.out.println("No valid order promo codes available.");
            throw new EcommerceException("No valid order promo codes available.");
        }

        // Map promo codes to response DTOs
        return promos.stream()
                .map(promo -> new PromoCodeResponse(
                        promo.getCode(),
                        promo.getPromoType().name(),
                        promo.getDiscountPercentage(),
                        null, // productId is not applicable for order promos
                        promo.getMinOrderAmount(),
                        Timestamp.valueOf(promo.getValidFrom()),
                        Timestamp.valueOf(promo.getValidTo())
                ))
                .collect(Collectors.toList());
    }

    // Method to apply a promo code and calculate the discounted total amount
    @Override
    public BigDecimal applyPromoCode(String promoCode, BigDecimal totalAmount) {
        // Log the action
        System.out.println("Applying promo code: " + promoCode);

        PromoCode promo = promoCodeRepository.findByCode(promoCode)
                .orElseThrow(() -> {
                    // Log error if promo code is invalid
                    System.out.println("Error: Invalid promo code: " + promoCode);
                    return new EcommerceException("Invalid Promo Code");
                });

        // Calculate the discount amount
        BigDecimal discountAmount = totalAmount.multiply(promo.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
        BigDecimal finalAmount = totalAmount.subtract(discountAmount);

        // Log the final amount after applying the promo code
        System.out.println("Final amount after applying promo code " + promoCode + ": " + finalAmount);
        return finalAmount;
    }

    // Method to get all promo codes
    @Override
    public List<PromoCodeResponse> getAllPromoCodes() {
        // Log the action
        System.out.println("Fetching all promo codes.");

        return promoCodeRepository.findAll().stream()
                .map(promo -> new PromoCodeResponse(
                        promo.getCode(),
                        promo.getPromoType().name(),
                        promo.getDiscountPercentage(),
                        promo.getProduct() != null ? promo.getProduct().getId() : null,
                        promo.getMinOrderAmount(),
                        Timestamp.valueOf(promo.getValidFrom()), // Ensure Timestamp conversion
                        Timestamp.valueOf(promo.getValidTo())
                ))
                .collect(Collectors.toList());
    }

    // Method to mark expired promo codes as EXPIRED
    @Override
    public void markExpiredPromoCodes() {
        // Log the action
        System.out.println("Marking expired promo codes.");

        List<PromoCode> expiredPromoCodes = promoCodeRepository.findExpiredPromoCodes(PromoStatus.ACTIVE);
        for (PromoCode promoCode : expiredPromoCodes) {
            promoCode.setStatus(PromoStatus.EXPIRED_DUE_TO_DATE);
            // Save the expired promo code status
            promoCodeRepository.save(promoCode);
            // Log each expired promo code
            System.out.println("Promo code expired: " + promoCode.getCode());
        }
    }
}
