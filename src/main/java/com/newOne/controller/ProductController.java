package com.newOne.controller;

import com.newOne.request.ProductRequest;
import com.newOne.response.ProductResponse;
import com.newOne.security.RoleAccessUtil;
import com.newOne.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing product-related operations such as:
 * - Adding, updating, and deleting products
 * - Fetching product details with filtering options
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RoleAccessUtil roleAccessUtil;

    /**
     * Add a new product (Admin only).
     *
     * @param request     Product details to be added
     * @param httpRequest HTTP request for access control
     * @return Success message or access denied
     */
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request, HttpServletRequest httpRequest) {
        log.info("Attempting to add a new product...");

        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied while adding a product.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = productService.addProduct(request);
        log.info("Product added successfully: {}", request.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing product (Admin only).
     *
     * @param id          Product ID to update
     * @param request     Updated product details
     * @param httpRequest HTTP request for access control
     * @return Success message or access denied
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id,
                                                @RequestBody ProductRequest request,
                                                HttpServletRequest httpRequest) {
        log.info("Attempting to update product with ID: {}", id);

        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied while updating product with ID: {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = productService.updateProduct(id, request);
        log.info("Product updated successfully with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product (Admin only).
     *
     * @param id          Product ID to delete
     * @param httpRequest HTTP request for access control
     * @return Success message or access denied
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id, HttpServletRequest httpRequest) {
        log.info("Attempting to delete product with ID: {}", id);

        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied while deleting product with ID: {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = productService.deleteProduct(id);
        log.info("Product deleted successfully with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get product list with optional filters (Accessible to both Admin and User).
     *
     * @param name        Filter by product name
     * @param sku         Filter by SKU
     * @param categoryId  Filter by category ID
     * @param status      Filter by status
     * @param pageable    Pagination details
     * @param httpRequest HTTP request for access control
     * @return List of filtered products or access denied
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String status,
            Pageable pageable,
            HttpServletRequest httpRequest) {

        log.info("Fetching product list with filters - Name: {}, SKU: {}, Category ID: {}, Status: {}",
                name, sku, categoryId, status);

        if (!roleAccessUtil.hasAccessForBoth(httpRequest)) {
            log.warn("Access denied while fetching product list.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<ProductResponse> products = productService.getProducts(name, sku, categoryId, status, pageable);
        log.info("Fetched {} products successfully.", products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * Get product details by ID (Accessible to both Admin and User).
     *
     * @param id          Product ID
     * @param httpRequest HTTP request for access control
     * @return Product details or access denied
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id, HttpServletRequest httpRequest) {
        log.info("Fetching product details for ID: {}", id);

        if (!roleAccessUtil.hasAccessForBoth(httpRequest)) {
            log.warn("Access denied while fetching product details for ID: {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        ProductResponse product = productService.getProductById(id);
        log.info("Product details fetched successfully for ID: {}", id);
        return ResponseEntity.ok(product);
    }
}
