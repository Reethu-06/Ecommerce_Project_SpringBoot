package com.newOne.response;

import com.newOne.entity.Status;

import java.math.BigDecimal;

public class OrderResponse {
    private Long userId;
    private Long productId;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String orderStatus;

//    public OrderResponse(Long userId, Long productId, BigDecimal totalAmount,
//                         String paymentStatus, String orderStatus) {
//        this.userId = userId;
//        this.productId = productId;
//        this.totalAmount = totalAmount;
//        this.paymentStatus = paymentStatus;
//        this.orderStatus = orderStatus;
//    }

    public OrderResponse(int userId, BigDecimal totalAmount, Status orderStatus) {
        this.userId = (long) userId;
        this.totalAmount = totalAmount;
        this.orderStatus = String.valueOf(orderStatus);
    }


    // Getters and Setters


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
