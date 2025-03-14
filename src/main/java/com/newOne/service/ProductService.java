package com.newOne.service;

import com.newOne.request.ProductRequest;
import com.newOne.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface ProductService {

    /**
     * Adds a new product to the system.
     * The product details will be added as specified in the request.
     *
     * @param request The product details to be added (name, SKU, category, price, etc.).
     * @return A success or failure message after attempting to add the product.
     */
    String addProduct(ProductRequest request);

    /**
     * Updates an existing product with the provided details.
     * The product's information is updated based on the ID and the new details.
     *
     * @param id The ID of the product to be updated.
     * @param request The new product details to update.
     * @return A success or failure message after attempting to update the product.
     */
    String updateProduct(Integer id, ProductRequest request);

    /**
     * Deletes an existing product by its ID.
     * This action may involve soft deleting or archiving the product.
     *
     * @param id The ID of the product to be deleted.
     * @return A success or failure message after attempting to delete the product.
     */
    String deleteProduct(Integer id);

    /**
     * Retrieves a product by its ID.
     * This method is used to fetch detailed information of a single product.
     *
     * @param id The ID of the product to be fetched.
     * @return A ProductResponse containing the details of the product.
     */
    ProductResponse getProductById(Integer id);

    /**
     * Retrieves a list of products, with optional filtering by name, SKU, category, or status.
     * Pagination support is provided to limit the number of products returned per request.
     *
     * @param name The name of the product (optional filter).
     * @param sku The SKU of the product (optional filter).
     * @param categoryId The category ID of the product (optional filter).
     * @param status The status of the product (optional filter).
     * @param pageable The pagination details (e.g., page number and size).
     * @return A paginated list of products matching the provided filters.
     */
    List<ProductResponse> getProducts(String name, String sku, Integer categoryId, String status, Pageable pageable);
}