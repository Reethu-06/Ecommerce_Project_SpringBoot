package com.newOne.repository;

import com.newOne.entity.User;
import com.newOne.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Wallet entities.
 * Provides methods for querying wallet data for users from the database.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    /**
     * Find a wallet by the associated user.
     *
     * @param user the user whose wallet is to be found.
     * @return an Optional containing the wallet if found.
     */
    Optional<Wallet> findByUser(User user);

    /**
     * Find a wallet by the user’s ID (Long).
     *
     * @param userId the ID of the user whose wallet is to be found.
     * @return an Optional containing the wallet if found.
     */
    Optional<Wallet> findByUserId(Long userId);

    /**
     * Find a wallet by the user’s ID (int).
     *
     * @param userId the ID of the user whose wallet is to be found.
     * @return an Optional containing the wallet if found.
     */
    Optional<Wallet> findByUserId(int userId);
}
