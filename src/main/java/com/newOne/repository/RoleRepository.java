package com.newOne.repository;

import com.newOne.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * Provides methods to interact with Role data in the database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a Role by its name.
     *
     * @param roleName The name of the role to search for.
     * @return An Optional containing the Role if found, otherwise empty.
     */
    Optional<Role> findByRoleName(String roleName);
}
