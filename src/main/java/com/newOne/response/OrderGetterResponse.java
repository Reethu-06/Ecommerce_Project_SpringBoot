package com.newOne.response;

import com.newOne.response.OrderItemResponse;

import java.math.BigDecimal;
import java.util.List;

public class OrderGetterResponse {
    private Long userId;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> orderItems;
    private String currentStatus;

    // Getters
    public Long getUserId() {
        return userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    // Constructor
    public OrderGetterResponse(Long userId, BigDecimal totalAmount, List<OrderItemResponse> orderItems, String currentStatus) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
        this.currentStatus = currentStatus;
    }

    public OrderGetterResponse() {}
}
