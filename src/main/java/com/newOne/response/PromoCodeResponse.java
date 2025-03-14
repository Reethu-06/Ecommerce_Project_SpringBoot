package com.newOne.response;

import com.newOne.entity.PromoType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PromoCodeResponse {
    private String code;
    private PromoType promoType;
    private BigDecimal discountPercentage;
    private Integer productId;
    private BigDecimal minOrderAmount;
    private Timestamp validFrom;
    private Timestamp validTo;

    // Default Constructor
    public PromoCodeResponse() {}


    public PromoCodeResponse(String code, PromoType promoType, BigDecimal discountPercentage, Integer productId, BigDecimal minOrderAmount, Timestamp validFrom, Timestamp validTo) {
        this.code = code;
        this.promoType = promoType;
        this.discountPercentage = discountPercentage;
        this.productId = productId;
        this.minOrderAmount = minOrderAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public PromoCodeResponse(String code, String promoType, BigDecimal discountPercentage, Integer productId, BigDecimal minOrderAmount, Timestamp validFrom, Timestamp validTo) {
        this.code = code;
        this.promoType = PromoType.valueOf(promoType); // Convert String to Enum
        this.discountPercentage = discountPercentage;
        this.productId = productId;
        this.minOrderAmount = minOrderAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }


    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public PromoType getPromoType() { return promoType; }
    public void setPromoType(PromoType promoType) { this.promoType = promoType; }

    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public BigDecimal getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(BigDecimal minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public Timestamp getValidFrom() { return validFrom; }
    public void setValidFrom(Timestamp validFrom) { this.validFrom = validFrom; }

    public Timestamp getValidTo() { return validTo; }
    public void setValidTo(Timestamp validTo) { this.validTo = validTo; }
}
