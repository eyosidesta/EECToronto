package com.example.EECToronto.TeamSMS;

import com.example.EECToronto.Members.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.EECToronto.Members.Members;
import java.util.*;

@Service
public class TeamSMSService {
    private final TeamSMSRepository teamSMSRepository;
    private final MembersRepository membersRepository;
    @Autowired
    public TeamSMSService(TeamSMSRepository teamSMSRepository, MembersRepository membersRepository) {
        this.teamSMSRepository = teamSMSRepository;
        this.membersRepository = membersRepository;
    }
    public List<TeamSMS> getAllTeamSMSService() {
        return teamSMSRepository.findAll();
    }

    public void sendTeamSMSService(TeamSMS teamSMS, Members members) {
        Optional<Members> member = membersRepository.findById(1L);
        Members getMemberDetail = member.get();

        System.out.println("Member is: " + getMemberDetail.getName());
    }
}
