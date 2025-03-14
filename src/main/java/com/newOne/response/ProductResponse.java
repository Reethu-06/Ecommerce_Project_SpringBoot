package com.newOne.response;

import com.newOne.entity.ProductStatus;

public class ProductResponse {
    private String name;
    private Integer stockQuantity;
    private ProductStatus status;

    public ProductResponse(String name, Integer stockQuantity, ProductStatus status) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public ProductStatus getStatus() {
        return status;
    }
}
