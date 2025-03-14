package com.newOne.response;

public class OrderResponseDto {
    private String message;
    private int orderId;

    public OrderResponseDto(String message, int orderId) {
        this.message = message;
        this.orderId = orderId;
    }

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
