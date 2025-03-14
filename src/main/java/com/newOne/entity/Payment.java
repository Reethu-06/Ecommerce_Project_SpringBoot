package com.newOne.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment extends TrackingColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)  // Lazy loading for better performance
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;  // Changed from int to BigDecimal for decimal support

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    // Constructors
    public Payment() {}

    public Payment(Orders order, BigDecimal amount, PaymentStatus status) {
        this.order = order;
        this.amount = amount;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Orders getOrder() { return order; }
    public void setOrder(Orders order) { this.order = order; }

    public BigDecimal getAmount() { return amount; }  // Changed return type
    public void setAmount(BigDecimal amount) { this.amount = amount; }  // Changed parameter type

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
}
