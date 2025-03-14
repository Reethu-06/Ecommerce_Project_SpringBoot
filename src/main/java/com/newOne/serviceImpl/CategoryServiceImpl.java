package com.newOne.serviceImpl;

import com.newOne.entity.Category;
import com.newOne.repository.CategoryRepository;
import com.newOne.request.CategoryRequest;
import com.newOne.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Adds a new category to the repository.
     * @param request The request containing category data
     * @return Success message
     */
    @Override
    public String addCategory(CategoryRequest request) {
        // Log the incoming request to add a category
        log.info("Adding new category: {}", request.getName());

        // Create and set category properties
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        // Save the category to the database
        categoryRepository.save(category);

        // Log successful category addition
        log.info("Category added successfully: {}", category.getName());
        return "Category added successfully.";
    }

    /**
     * Updates an existing category by ID.
     * @param id The category ID to update
     * @param name The new name of the category
     * @param description The new description of the category
     * @return Success or error message
     */
    @Override
    public String updateCategory(int id, String name, String description) {
        // Log the category update attempt
        log.info("Updating category with ID: {}", id);

        return categoryRepository.findById(id).map(category -> {
            category.setName(name);
            category.setDescription(description);
            categoryRepository.save(category);

            // Log successful category update
            log.info("Category updated successfully: {}", category.getName());
            return "Category updated successfully.";
        }).orElse("Category not found.");
    }

    /**
     * Retrieves a list of category names with a limit.
     * @param limit The maximum number of categories to retrieve
     * @return List of category names
     */
    @Override
    public List<String> getCategories(int limit) {
        // Log the category retrieval request
        log.info("Fetching top {} categories.", limit);

        // Create pageable object for pagination
        Pageable pageable = PageRequest.of(0, limit);

        // Fetch and return a list of category names
        List<String> categoryNames = categoryRepository.findAll(pageable)
                .stream()
                .map(Category::getName) // Only fetching names
                .collect(Collectors.toList());

        // Log the successful fetch operation
        log.info("Fetched {} category names.", categoryNames.size());
        return categoryNames;
    }

    /**
     * Deletes a category by ID.
     * @param id The category ID to delete
     * @return Success or error message
     */
    @Override
    public String deleteCategory(int id) {
        // Log the category deletion attempt
        log.info("Attempting to delete category with ID: {}", id);

        return categoryRepository.findById(id).map(category -> {
            categoryRepository.deleteById(id);

            // Log successful category deletion
            log.info("Category deleted successfully: {}", category.getName());
            return "Category deleted successfully.";
        }).orElse("Category not found.");
    }
}
