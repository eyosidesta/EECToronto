package com.example.EECToronto.TeamSMS;

import com.example.EECToronto.Members.Members;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(path="api/team_sms")
public class TeamSMSController {
    private final TeamSMSService teamSMSService;
    @Autowired

    public TeamSMSController(TeamSMSService teamSMSService) {
        this.teamSMSService = teamSMSService;
    }
    @GetMapping
    public List<TeamSMS> getAllTeamSMS() {
        return teamSMSService.getAllTeamSMSService();
    }
    @PostMapping("/send_all")
    public void sendMessageForAll(@RequestBody String message) {
        teamSMSService.sendMessageForAll(message);
    }

    @PostMapping
    public void sendTeamSMS(TeamSMS teamSMS, Members members) {
        teamSMSService.sendTeamSMSService(teamSMS, members);
    }

}
