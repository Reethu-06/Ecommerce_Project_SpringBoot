package com.newOne.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class Transaction extends TrackingColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;


    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Default Constructor
    public Transaction() {
        super();
    }

    // Constructor with All Fields
    public Transaction(User user, Orders order, BigDecimal amount, TransactionType transactionType,
                       TransactionStatus status, PaymentMethod paymentMethod, String referenceId,
                       String description) {
        this.user = user;
        this.order = order;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.paymentMethod = paymentMethod;

        this.description = description;
    }

    // Constructor for Order Checkout Success
    public Transaction(User user, Orders order, BigDecimal totalAmount, TransactionType transactionType,
                       TransactionStatus transactionStatus, PaymentMethod paymentMethod,
                       String orderCheckoutSuccessful) {
        this.user = user;
        this.order = order;
        this.amount = totalAmount;
        this.transactionType = transactionType;
        this.status = transactionStatus;
        this.paymentMethod = paymentMethod;
        this.description = orderCheckoutSuccessful;
    }

    // Constructor for Basic Transaction with Only Required Fields
    public Transaction(User user, BigDecimal amount, TransactionType transactionType,
                       TransactionStatus status, PaymentMethod paymentMethod) {
        this.user = user;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }



    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Orders getOrder() { return order; }
    public void setOrder(Orders order) { this.order = order; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }


    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
