package com.newOne.repository;

import com.newOne.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on OrderItems entities.
 * This interface extends JpaRepository for basic database operations related to OrderItems.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Integer> {

    /**
     * Finds all order items associated with a specific order.
     *
     * @param orderId the ID of the order.
     * @return a list of OrderItems associated with the given orderId.
     */
    List<OrderItems> findByOrderId(Long orderId);
}
