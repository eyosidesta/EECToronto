package com.example.EECToronto.Teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/teams")
public class TeamsContorller {
    private final TeamsService teamsService;
    @Autowired
    public TeamsContorller(TeamsService teamsService) {
        this.teamsService = teamsService;
    }

    @GetMapping
    public List<Teams> getAllTeams() {
        return teamsService.getAllTeamsService();
    }

    @PostMapping
    public void addTeam(@RequestBody Teams team) {
        teamsService.addTeamService(team);
    }
}
