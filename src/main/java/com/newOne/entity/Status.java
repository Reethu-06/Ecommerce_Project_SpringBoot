package com.newOne.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "status", uniqueConstraints = {@UniqueConstraint(columnNames = "status_name")})
public class Status extends TrackingColumn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "status_name", length = 50, nullable = false, unique = true)
    private String statusName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Constructors
    public Status() {
        // Default constructor
    }

    public Status(String statusName, String description) {
        this.statusName = statusName;
        this.description = description;
    }
}
