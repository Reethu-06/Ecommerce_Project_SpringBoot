package com.newOne.serviceImpl;

import com.newOne.customException.EcommerceException;
import com.newOne.entity.Category;
import com.newOne.entity.Product;
import com.newOne.entity.ProductStatus;
import com.newOne.repository.CategoryRepository;
import com.newOne.repository.ProductRepository;
import com.newOne.request.ProductRequest;
import com.newOne.response.ProductResponse;
import com.newOne.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service to handle product related operations
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Method to add a new product
    @Override
    public String addProduct(ProductRequest request) {
        // Log input parameters
        System.out.println("Adding product with name: " + request.getName());

        // Fetch category for the product
        Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());
        if (!categoryOpt.isPresent()) {
            // Log error if category not found
            System.out.println("Error: Category not found with ID: " + request.getCategoryId());
            throw new EcommerceException("Category not found");
        }

        // Create product and set its details
        Product product = new Product();
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setCategory(categoryOpt.get());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase()));

        // Save product to repository
        productRepository.save(product);
        // Log success message
        System.out.println("Product added successfully: " + request.getName());
        return "Product added successfully!";
    }

    // Method to update an existing product
    @Override
    public String updateProduct(Integer id, ProductRequest request) {
        // Log input parameters
        System.out.println("Updating product with ID: " + id);

        // Find the product by ID
        Optional<Product> productOpt = productRepository.findById(id);
        if (!productOpt.isPresent()) {
            // Log error if product not found
            System.out.println("Error: Product not found with ID: " + id);
            throw new EcommerceException("Product not found");
        }

        // Get the product to update
        Product product = productOpt.get();
        // Update product fields based on request
        if (request.getName() != null) product.setName(request.getName());
        if (request.getSku() != null) product.setSku(request.getSku());
        if (request.getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());
            categoryOpt.ifPresent(product::setCategory);
        }
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStockQuantity() != null) product.setStockQuantity(request.getStockQuantity());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getStatus() != null) product.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase()));

        // Save the updated product
        productRepository.save(product);
        // Log success message
        System.out.println("Product updated successfully: " + id);
        return "Product updated successfully!";
    }

    // Method to delete a product (soft delete)
    @Override
    public String deleteProduct(Integer id) {
        // Log input parameters
        System.out.println("Deleting product with ID: " + id);

        // Find the product by ID
        Optional<Product> productOpt = productRepository.findById(id);
        if (!productOpt.isPresent()) {
            // Log error if product not found
            System.out.println("Error: Product not found with ID: " + id);
            throw new EcommerceException("Product not found");
        }

        // Get the product to delete and mark it as deleted
        Product product = productOpt.get();
        product.setDeleted(true);
        // Save the product with the deleted status
        productRepository.save(product);
        // Log success message
        System.out.println("Product deleted successfully: " + id);
        return "Product deleted successfully!";
    }

    // Method to get products based on filters
    @Override
    public List<ProductResponse> getProducts(String name, String sku, Integer categoryId, String status, Pageable pageable) {
        // Log input parameters
        System.out.println("Fetching products with filters - Name: " + name + ", SKU: " + sku + ", Category ID: " + categoryId + ", Status: " + status);

        // Fetch products from repository using filters
        Page<Product> products = productRepository.findByFilters(name, sku, categoryId, status, pageable);
        // Log number of products found
        System.out.println("Found " + products.getContent().size() + " products");

        // Return product responses
        return products.getContent().stream()
                .map(product -> new ProductResponse(product.getName(), product.getStockQuantity(), product.getStatus()))
                .collect(Collectors.toList());
    }

    // Method to get a product by its ID
    @Override
    public ProductResponse getProductById(Integer id) {
        // Log input parameters
        System.out.println("Fetching product with ID: " + id);

        // Find the product by ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    // Log error if product not found
                    System.out.println("Error: Product not found with ID: " + id);
                    return new EcommerceException("Product not found");
                });

        // Return product response
        return new ProductResponse(product.getName(), product.getStockQuantity(), product.getStatus());
    }
}
