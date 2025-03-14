package com.newOne.repository;

import com.newOne.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for handling Address entity data.
 * This repository provides CRUD operations for Address records.
 *
 * Custom query methods can be added here to interact with the Address table.
 */
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * Finds an address by the user ID.
     *
     * @param userId the ID of the user whose address needs to be fetched.
     * @return an Optional containing the Address if found, or empty if no address exists for the user.
     */
    Optional<Address> findByUserId(int userId);

    Optional<Address> findTopByUserIdOrderByCreatedAtDesc(int id);
}
