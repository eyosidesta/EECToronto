package com.example.EECToronto.Neighborhood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path="api/neighborhood")
public class NeighborhoodController {
    private final NeighborhoodService neighborhoodService;
    @Autowired
    public NeighborhoodController(NeighborhoodService neighborhoodService) {
        this.neighborhoodService = neighborhoodService;
    }

    @GetMapping
    public List<Neighborhood> getAllNeighborhood() {
        return neighborhoodService.getAllNeighborhoodService();
    }

    @GetMapping(path="/{neighborhood_id}")
    public Neighborhood getNeighborhood(@PathVariable Long neighborhood_id) {
        return neighborhoodService.getNeighrborhoodService(neighborhood_id);
    }

    @PostMapping
    public void addNeighborhood(@RequestBody Neighborhood neighborhood) {
        neighborhoodService.addNeighborhoodService(neighborhood);
    }
}
