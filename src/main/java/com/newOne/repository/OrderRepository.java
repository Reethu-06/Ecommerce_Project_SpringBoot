package com.newOne.repository;

import com.newOne.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Orders entities.
 * This interface extends JpaRepository to provide basic database operations related to Orders.
 */
@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {

    /**
     * Finds all orders associated with a specific user.
     *
     * @param userId the ID of the user.
     * @return a list of Orders associated with the given userId.
     */
    List<Orders> findByUserId(int userId);

    /**
     * Finds a specific order by user ID and order ID.
     *
     * @param userId  the ID of the user.
     * @param orderId the ID of the order.
     * @return a list of Orders matching the given userId and orderId.
     */
    List<Orders> findByUserIdAndId(Long userId, Long orderId);
}
