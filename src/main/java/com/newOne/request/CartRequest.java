package com.newOne.request;

import java.util.List;

// CartRequest DTO
public class CartRequest {
    private Long userId;
    private List<CartProductRequest> products;

    // Getters and Setters
    public Integer getUserId() { return Math.toIntExact(userId); }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<CartProductRequest> getProducts() { return products; }
    public void setProducts(List<CartProductRequest> products) { this.products = products; }
}