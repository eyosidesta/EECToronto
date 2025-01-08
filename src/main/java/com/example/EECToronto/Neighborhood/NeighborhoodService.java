package com.example.EECToronto.Neighborhood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NeighborhoodService {
    private final NeighborhoodRepository neighborhoodRepository;
    @Autowired
    public NeighborhoodService(NeighborhoodRepository neighborhoodRepository) {
        this.neighborhoodRepository = neighborhoodRepository;
    }

    public List<Neighborhood> getAllNeighborhoodService() {
        return neighborhoodRepository.findAll();
    }

    public Neighborhood getNeighrborhoodService(Long neighborhood_id) {
        return neighborhoodRepository.findById(neighborhood_id).orElseThrow(() -> new IllegalArgumentException("Invalid Neighborhood Id: " + neighborhood_id));
    }

    public void addNeighborhoodService(Neighborhood neighborhood) {
        neighborhoodRepository.save(neighborhood);
    }
}
