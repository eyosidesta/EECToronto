package com.example.EECToronto.TeamMembers;

import com.example.EECToronto.Members.Members;
import com.example.EECToronto.Teams.Teams;
import jakarta.persistence.*;

@Entity
@Table
public class TeamMembers {
    @Id
    @SequenceGenerator(
            name = "team_members_sequence",
            sequenceName = "team_members_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_members_sequence"
    )

    private Long id;
    @ManyToOne
    @JoinColumn(name="team_id", nullable = false, foreignKey = @ForeignKey(name="team_member_fkey"))
    private Teams teams;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false, foreignKey = @ForeignKey(name="member_team_fkey"))
    private Members members;

    public TeamMembers() {}
    public TeamMembers(Teams teams, Members members) {
        this.teams = teams;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public Teams getTeam() {
        return teams;
    }
    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public Members getMembers() {
        return members;
    }
    public void setMembers(Members members) {
        this.members = members;
    }

}
