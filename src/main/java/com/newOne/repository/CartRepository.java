package com.newOne.repository;

import com.newOne.entity.Cart;
import com.newOne.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling Cart entity data.
 * This repository provides CRUD operations for Cart records.
 * It includes methods to interact with cart data based on a user's ID and product ID.
 */
public interface CartRepository extends JpaRepository<Cart, Integer> {

    /**
     * Finds all cart items for a given user.
     *
     * @param user the user whose cart items are to be fetched.
     * @return a list of Cart entities associated with the user.
     */
    List<Cart> findByUser(User user);

    /**
     * Finds all cart items for a user by user ID.
     *
     * @param userId the ID of the user whose cart items are to be fetched.
     * @return a list of Cart entities associated with the given user ID.
     */
 //   List<Cart> findByUserId(int userId);

    List<Cart> findByUserId(Long userId);

//    @Query("SELECT c FROM Cart c WHERE c.userId = :userId")
//    Optional<Cart> findByUserId(@Param("userId") Long userId);


    /**
     * Deletes all cart items for a given user by user ID.
     *
     * @param userId the ID of the user whose cart items need to be deleted.
     */
    void deleteByUserId(int userId);

    /**
     * Finds a specific cart item by user ID and product ID.
     *
     * @param userId the ID of the user.
     * @param productId the ID of the product.
     * @return an Optional containing the Cart entity if it exists, otherwise empty.
     */
    Optional<Cart> findByUserIdAndProductId(Integer userId, Integer productId);
}
