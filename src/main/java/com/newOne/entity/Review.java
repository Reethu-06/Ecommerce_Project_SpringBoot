package com.newOne.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review extends TrackingColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    // Constructors
    public Review() {}

    public Review(User user, Product product, int rating, String reviewText) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}

