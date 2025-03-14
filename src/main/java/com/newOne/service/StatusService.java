package com.newOne.service;

import com.newOne.entity.Status;

import java.util.Optional;


public interface StatusService {

    /**
     * Retrieves the status object based on the provided status name.
     *
     * @param statusName The name of the status to fetch.
     * @return An Optional containing the status if found, otherwise an empty Optional.
     */
    Optional<Status> getStatusByName(String statusName);
}