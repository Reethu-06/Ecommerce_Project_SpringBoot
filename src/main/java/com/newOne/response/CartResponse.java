package com.newOne.response;

import java.math.BigDecimal;

public class CartResponse {
    private Integer cartId;
    private Integer userId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;

    public CartResponse(Integer cartId, Integer userId, Integer productId, String productName, Integer quantity, BigDecimal price) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public Integer getCartId() { return cartId; }
    public Integer getUserId() { return userId; }
    public Integer getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
}
