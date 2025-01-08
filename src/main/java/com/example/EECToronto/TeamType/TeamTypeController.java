package com.example.EECToronto.TeamType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="api/team_type")
public class TeamTypeController {
    private final TeamTypeService teamTypeService;
    @Autowired
    public TeamTypeController(TeamTypeService teamTypeService) {
        this.teamTypeService = teamTypeService;
    }
    @GetMapping
    public List<TeamType> getAllTeamType() {
        return teamTypeService.getAllTeamTypeService();
    }
}
