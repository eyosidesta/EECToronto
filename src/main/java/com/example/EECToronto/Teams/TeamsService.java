package com.example.EECToronto.Teams;

import com.example.EECToronto.TeamType.TeamType;
import com.example.EECToronto.TeamType.TeamTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamsService {
    private final TeamsRepository teamsRepository;
    @Autowired
    public TeamsService(TeamsRepository teamsRepository) {
        this.teamsRepository = teamsRepository;
    }
    public List<Teams> getAllTeamsService() {
        return teamsRepository.findAll();
    }
    public void addTeamService(Teams team) {
        teamsRepository.save(team);
    }
}
