//package com.newOne.controller;
//
//import com.newOne.request.CategoryRequest;
//import com.newOne.service.CategoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/category")
//public class CategoryController {
//
//    @Autowired
//    private CategoryService categoryService;
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest request) {
//        String response = categoryService.addCategory(request);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @PutMapping("/update/{id}")
//    public String updateCategory(@PathVariable int id, @RequestParam String name, @RequestParam String description) {
//        return categoryService.updateCategory(id, name, description);
//    }
//
//    @GetMapping("/getAll")
//    public String getCategories(@RequestParam(defaultValue = "5") int limit) {
//        List<String> categories = categoryService.getCategories(limit);
//
//        // Display one by one instead of a list
//        return String.join("\n", categories);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public String deleteCategory(@PathVariable int id) {
//        return categoryService.deleteCategory(id);
//    }
//}

package com.newOne.controller;

import com.newOne.request.CategoryRequest;
import com.newOne.security.RoleAccessUtil;
import com.newOne.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j; // Logger for better tracing
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing category-related operations.
 * Handles adding, updating, retrieving, and deleting categories.
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RoleAccessUtil roleAccessUtil;

    /**
     * Adds a new category (Admin access only).
     *
     * @param request      Category details in request body.
     * @param httpRequest  Request to extract admin token.
     * @return             Response message indicating success or failure.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest request, HttpServletRequest httpRequest) {
        log.info("Received request to add category: {}", request);

        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied while adding category.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = categoryService.addCategory(request);
        log.info("Category added successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing category (Admin access only).
     *
     * @param id            Category ID to update.
     * @param name          New category name.
     * @param description   New category description.
     * @param httpRequest   Request to extract admin token.
     * @return              Response message indicating success or failure.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id,
                                                 @RequestParam String name,
                                                 @RequestParam String description,
                                                 HttpServletRequest httpRequest) {
        log.info("Received request to update category. ID: {}, Name: {}, Description: {}", id, name, description);

        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied while updating category ID: {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = categoryService.updateCategory(id, name, description);
        log.info("Category updated successfully. ID: {}", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a list of categories. Accessible to both Admin and User.
     *
     * @param limit         Number of categories to fetch (default 5).
     * @param httpRequest   Request to extract role token.
     * @return              List of categories or access denied response.
     */
    @GetMapping("/getAll")
    public ResponseEntity<String> getCategories(@RequestParam(defaultValue = "5") int limit,
                                                HttpServletRequest httpRequest) {
        log.info("Received request to fetch categories with limit: {}", limit);

        if (!roleAccessUtil.hasAdminAccess(httpRequest) && !roleAccessUtil.hasUserAccess(httpRequest)) {
            log.warn("Access denied while fetching categories.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        List<String> categories = categoryService.getCategories(limit);
        log.info("Fetched {} categories successfully.", categories.size());
        return ResponseEntity.ok(String.join("\n", categories));
    }

    /**
     * Deletes a category by ID (Admin access only).
     *
     * @param id            Category ID to delete.
     * @param httpRequest   Request to extract admin token.
     * @return              Response message indicating success or failure.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id, HttpServletRequest httpRequest) {
        log.info("Received request to delete category ID: {}", id);

        if (!roleAccessUtil.hasAdminAccess(httpRequest)) {
            log.warn("Access denied while deleting category ID: {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String response = categoryService.deleteCategory(id);
        log.info("Category deleted successfully. ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
