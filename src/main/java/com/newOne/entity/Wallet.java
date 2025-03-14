package com.newOne.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet extends TrackingColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "balance", nullable = false)
    private Integer balance;  // No decimals

    // Constructors
    public Wallet() {
    }

    public Wallet(User user, int balance) {
        this.user = user;
        this.balance = balance;
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

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
