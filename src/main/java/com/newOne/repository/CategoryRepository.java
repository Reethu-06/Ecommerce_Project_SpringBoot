package com.newOne.repository;

import com.newOne.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on Category entities.
 * This interface extends JpaRepository for basic database operations.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /**
     * Method to check if a category with the given name already exists in the database.
     *
     * @param name the name of the category.
     * @return true if the category exists, false otherwise.
     */
    boolean existsByName(String name);

    // You can add additional custom queries or methods as needed here.
}
