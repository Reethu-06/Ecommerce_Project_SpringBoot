package com.newOne.repository;

import com.newOne.entity.WalletAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing WalletAudit entities.
 * Provides methods for querying wallet transaction audit data from the database.
 */
@Repository
public interface WalletAuditRepository extends JpaRepository<WalletAudit, Long> {
    // This repository extends JpaRepository, so no additional methods are required here.
}
