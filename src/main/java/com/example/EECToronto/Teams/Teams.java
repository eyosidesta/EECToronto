package com.example.EECToronto.Teams;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.TeamMembers.TeamMembers;
import com.example.EECToronto.TeamSMS.TeamSMS;
import com.example.EECToronto.TeamType.TeamType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="teams")
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String team_name;
    private String team_description;

    @OneToMany(mappedBy = "teams", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMembers> team_members = new ArrayList<>();

    public Teams(){}

    public Teams (String team_name, String team_description) {
        this.team_name = team_name;
        this.team_description = team_description;
    }

    public Long getId() {
        return id;
    }

    public String getTeam_name() {
        return this.team_name;
    }
    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_description() {
        return this.team_description;
    }

    public void setTeam_description(String team_description) {
        this.team_description = team_description;
    }

}
