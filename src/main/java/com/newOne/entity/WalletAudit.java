package com.newOne.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_audit")
public class WalletAudit extends TrackingColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private TransactionReason reason;

    // Default Constructor
    public WalletAudit() {}

    // Constructor for Standard Transactions
    public WalletAudit(User user, Wallet wallet, BigDecimal amount,
                       TransactionType transactionType, TransactionReason reason) {
        this.user = user;
        this.wallet = wallet;
        this.amount = amount;
        this.transactionType = transactionType;
        this.reason = reason;
    }

    // Constructor for Wallet Deduction (e.g., Order Payment)
    public WalletAudit(User user, Wallet wallet, BigDecimal amount, TransactionReason reason) {
        this.user = user;
        this.wallet = wallet;
        this.amount = amount;
        this.transactionType = TransactionType.DEBIT; // Auto-set for deduction
        this.reason = reason;
    }

    // Constructor for Refunds or Credit Transactions
    public WalletAudit(User user, Wallet wallet, BigDecimal amount,
                       TransactionType transactionType) {
        this.user = user;
        this.wallet = wallet;
        this.amount = amount;
        this.transactionType = transactionType;
        this.reason = TransactionReason.REFUND; // Auto-set for refunds
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionReason getReason() {
        return reason;
    }

    public void setReason(TransactionReason reason) {
        this.reason = reason;
    }
}
