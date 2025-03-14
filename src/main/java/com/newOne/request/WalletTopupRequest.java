package com.newOne.request;

import java.math.BigDecimal;

public class WalletTopupRequest {
    private int userId;
    private BigDecimal amount;

    public WalletTopupRequest() {}

    public WalletTopupRequest(int userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
