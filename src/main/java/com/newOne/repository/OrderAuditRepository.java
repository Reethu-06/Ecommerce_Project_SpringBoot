package com.newOne.repository;

import com.newOne.entity.OrderAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on OrderAudit entities.
 * This interface extends JpaRepository for basic database operations related to OrderAudit.
 */
@Repository
public interface OrderAuditRepository extends JpaRepository<OrderAudit, Long> {
    // Custom queries or methods can be added here if needed in the future.
}
