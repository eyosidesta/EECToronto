package com.example.EECToronto.TeamType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamTypeService {
    private final TeamTypeRepository teamTypeRepository;
    @Autowired
    public TeamTypeService(TeamTypeRepository teamTypeRepository) {
        this.teamTypeRepository = teamTypeRepository;
    }

    public List<TeamType> getAllTeamTypeService() {
        return teamTypeRepository.findAll();
    }
}
