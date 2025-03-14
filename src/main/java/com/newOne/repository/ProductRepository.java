package com.newOne.repository;

import com.newOne.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on Product entities.
 * This repository includes a custom query method to filter products by various attributes.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Custom query to find products based on various filter parameters.
     *
     * @param name      The name of the product to filter (case-insensitive).
     * @param sku       The SKU of the product.
     * @param categoryId The category ID of the product.
     * @param status    The status of the product (e.g., ACTIVE, INACTIVE).
     * @param pageable  Pagination details for the query.
     * @return          A Page of filtered products matching the provided criteria.
     */
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:sku IS NULL OR p.sku = :sku) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "p.isDeleted = false")
    Page<Product> findByFilters(
            @Param("name") String name,
            @Param("sku") String sku,
            @Param("categoryId") Integer categoryId,
            @Param("status") String status,
            Pageable pageable);
}
