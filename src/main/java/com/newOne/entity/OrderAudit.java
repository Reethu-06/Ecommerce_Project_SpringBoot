package com.newOne.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_audit")
public class OrderAudit extends TrackingColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "past_status_id",nullable = true)
    private Status pastStatus;

    @ManyToOne
    @JoinColumn(name = "current_status_id", nullable = false)
    private Status currentStatus;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    // Constructors
    public OrderAudit() {}

    public OrderAudit(Orders order, Status pastStatus, Status currentStatus, String reason) {
        this.order = order;
        this.pastStatus = pastStatus;
        this.currentStatus = currentStatus;
        this.reason = reason;
    }

    public OrderAudit(Orders order, Status pastStatus, Status currentStatus) {
        this.order = order;
        this.pastStatus = pastStatus;
        this.currentStatus = currentStatus;
    }
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Orders getOrder() { return order; }
    public void setOrder(Orders order) { this.order = order; }

    public Status getPastStatus() { return pastStatus; }
    public void setPastStatus(Status pastStatus) { this.pastStatus = pastStatus; }

    public Status getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(Status currentStatus) { this.currentStatus = currentStatus; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

