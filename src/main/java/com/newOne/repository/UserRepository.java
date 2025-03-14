package com.newOne.repository;

import com.newOne.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Provides methods for querying User-related data from the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find a user by their username.
     *
     * @param username the username to search for.
     * @return an Optional containing the user if found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their email address.
     *
     * @param email the email to search for.
     * @return an Optional containing the user if found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by their email.
     *
     * @param email the email to check.
     * @return true if the user exists with the given email, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Find the roleId of a user by their email address.
     * This is a custom query that returns only the roleId associated with the email.
     *
     * @param email the email of the user.
     * @return the roleId of the user.
     */
    @Query("SELECT u.roleId FROM User u WHERE u.email = :email")
    Integer findRoleByEmail(String email); // ✅ No need for @Param

    /**
     * Find a user by their phone number.
     *
     * @param phoneNumber the phone number to search for.
     * @return an Optional containing the user if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}
