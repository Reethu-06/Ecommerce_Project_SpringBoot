package com.newOne.service;

import com.newOne.request.CategoryRequest;

import java.util.List;


public interface CategoryService {

    /**
     * Adds a new category to the system.
     *
     * @param request Contains the details of the category (name, description, etc.).
     * @return A success message indicating that the category has been successfully added.
     */
    String addCategory(CategoryRequest request);

    /**
     * Updates the details of an existing category.
     *
     * @param id The ID of the category to be updated.
     * @param name The new name for the category.
     * @param description The new description for the category.
     * @return A success message indicating that the category has been successfully updated.
     */
    String updateCategory(int id, String name, String description);

    /**
     * Retrieves a list of category names with pagination.
     *
     * @param limit The number of categories to retrieve (for pagination).
     * @return A list of category names.
     */
    List<String> getCategories(int limit);

    /**
     * Deletes a category from the system.
     *
     * @param id The ID of the category to be deleted.
     * @return A success message indicating that the category has been successfully deleted.
     */
    String deleteCategory(int id);
}
