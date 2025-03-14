package com.newOne.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.newOne.entity.OrderAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on OrderAudit entities.
 * This interface extends JpaRepository for basic database operations related to OrderAudit.
 */
@Repository
public interface OrderAuditRepository extends JpaRepository<OrderAudit, Long> {
   // Remapper findTopByOrderIdOrderByUpdatedAtDesc(int id);
   Optional<OrderAudit> findTopByOrderIdOrderByUpdatedAtDesc(Long orderId);
    // Custom queries or methods can be added here if needed in the future.
}
