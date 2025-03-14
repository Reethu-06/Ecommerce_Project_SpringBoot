package com.newOne.repository;

import com.newOne.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Transaction entities.
 * Provides basic CRUD operations for the Transaction entity.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // No custom methods defined; this repository inherits basic CRUD functionality
}
