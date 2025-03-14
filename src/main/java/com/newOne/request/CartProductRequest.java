package com.newOne.request;

import jakarta.persistence.criteria.CriteriaBuilder;

public class CartProductRequest {
    private Integer productId;
    private int quantity;

    // Getters and Setters
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}