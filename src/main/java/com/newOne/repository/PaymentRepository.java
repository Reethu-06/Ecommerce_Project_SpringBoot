package com.newOne.repository;

import com.newOne.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on Payment entities.
 * This interface extends JpaRepository to provide basic database operations related to Payments.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // No custom queries or logic required as of now.
}
