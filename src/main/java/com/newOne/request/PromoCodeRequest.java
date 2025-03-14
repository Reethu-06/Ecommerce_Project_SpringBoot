package com.newOne.request;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PromoCodeRequest {
    private String code;
    private String promoType; // ORDER or PRODUCT
    private Integer productId; // Required for PRODUCT type
    private BigDecimal discountPercentage;
    private BigDecimal minOrderAmount; // Required for ORDER type
    private Timestamp validFrom;
    private Timestamp validTo;

    // Default Constructor
    public PromoCodeRequest() {}

    // Parameterized Constructor
    public PromoCodeRequest(String code, String promoType, Integer productId, BigDecimal discountPercentage, BigDecimal minOrderAmount, Timestamp validFrom, Timestamp validTo) {
        this.code = code;
        this.promoType = promoType;
        this.productId = productId;
        this.discountPercentage = discountPercentage;
        this.minOrderAmount = minOrderAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getPromoType() { return promoType; }
    public void setPromoType(String promoType) { this.promoType = promoType; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    public BigDecimal getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(BigDecimal minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public Timestamp getValidFrom() { return validFrom; }
    public void setValidFrom(Timestamp validFrom) { this.validFrom = validFrom; }

    public Timestamp getValidTo() { return validTo; }
    public void setValidTo(Timestamp validTo) { this.validTo = validTo; }
}

