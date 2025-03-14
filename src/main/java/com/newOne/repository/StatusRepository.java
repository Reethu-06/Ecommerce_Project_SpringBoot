package com.newOne.repository;

import com.newOne.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Status entities.
 * Provides methods to interact with Status data in the database.
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

   /**
    * Finds a Status by its name.
    *
    * @param statusName The name of the status to search for.
    * @return An Optional containing the Status if found, otherwise empty.
    */
   Optional<Status> findByStatusName(String statusName);
}
