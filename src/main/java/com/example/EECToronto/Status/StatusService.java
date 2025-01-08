package com.example.EECToronto.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private final StatusRepository statusRepository;
    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> getAllStatusService() {
        return statusRepository.findAll();
    }

    public Status getStatusByIdService(Long status_id) {
        return statusRepository.findById(status_id).orElseThrow(() -> new IllegalArgumentException("Invalid Status Id: " + status_id));
    }

    public void addStatus(Status status) {
        statusRepository.save(status);
    }
}
