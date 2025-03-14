package com.newOne.serviceImpl;

import com.newOne.entity.Status;
import com.newOne.repository.StatusRepository;
import com.newOne.service.StatusService;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Service to handle Status-related operations
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    // Constructor injection of the StatusRepository
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    // Method to get a Status by its name
    @Override
    public Optional<Status> getStatusByName(String statusName) {
        // Log the action of fetching the status by its name
        System.out.println("Fetching status by name: " + statusName);

        // Retrieve the status from the repository
        Optional<Status> status = statusRepository.findByStatusName(statusName);

        // Log the result of the fetch operation
        if (status.isPresent()) {
            System.out.println("Status found: " + status.get().getStatusName());
        } else {
            System.out.println("No status found with the name: " + statusName);
        }

        return status;
    }
}
