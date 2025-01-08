package com.example.EECToronto.Teams;

import com.example.EECToronto.TeamType.TeamType;
import com.example.EECToronto.TeamType.TeamTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamsService {
    private final TeamsRepository teamsRepository;
    private final TeamTypeRepository teamTypeRepository;
    @Autowired
    public TeamsService(TeamsRepository teamsRepository, TeamTypeRepository teamTypeRepository) {
        this.teamsRepository = teamsRepository;
        this.teamTypeRepository = teamTypeRepository;
    }
    public List<Teams> getAllTeamsService() {
        return teamsRepository.findAll();
    }
    public void addTeamService(Teams team) {
        if (team.getTeam_type() != null) {
            TeamType team_type = team.getTeam_type();
            if (team_type.getId() != null) {
                team_type = teamTypeRepository.findById(team_type.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Team Type Id"));
            } else {
                teamTypeRepository.save(team_type);
            }
            team.setTeam_type(team_type);

        }
        teamsRepository.save(team);
    }
}
