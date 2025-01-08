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
    @SequenceGenerator(
            name="team_sequence",
            sequenceName = "team_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_sequence"
    )
    private Long id;
    @Column(length = 255, nullable = false)
    private String team_name;
    private String team_description;
    @ManyToOne
    @JoinColumn(name="team_type_id", foreignKey = @ForeignKey(name="team_type_fkey"), nullable = true)
    private TeamType team_type;

    @OneToMany(mappedBy="teams", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TeamSMS> team_sms = new ArrayList<>();

    @OneToMany(mappedBy = "teams", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMembers> team_members = new ArrayList<>();

    public Teams(){}

    public Teams (String team_name, String team_description, TeamType team_type) {
        this.team_name = team_name;
        this.team_description = team_description;
        this.team_type = team_type;
    }

    public Long getId() {
        return id;
    }

    public TeamType getTeam_type() {
        return  team_type;
    }
    public void setTeam_type(TeamType team_type) {
        this.team_type = team_type;
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
