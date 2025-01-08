package com.example.EECToronto.TeamSMS;

import com.example.EECToronto.Teams.Teams;
import jakarta.persistence.*;
import com.example.EECToronto.Members.Members;

@Entity
@Table(name="team_sms")
public class TeamSMS {
    @Id
    @SequenceGenerator(
            name="team_sms_sequence",
            sequenceName="team_sms_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE,
            generator="team_sms_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String message;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="author_id", foreignKey = @ForeignKey(name = "author_sms_fkey"), nullable = true)
    private Members members;

    @ManyToOne
    @JoinColumn(name="team_id", foreignKey = @ForeignKey(name="team_sms_fkey"), nullable = false)
    private Teams teams;

    public TeamSMS() {};

    public TeamSMS(String message, Members members, Teams teams) {
        this.message = message;
        this.members = members;
        this.teams = teams;

    }

    public Members getMembers() {
        return members;
    }

    public void setmembers(Members members) {
        this.members = members;
    }

    public Teams getTeams() {
        return teams;
    }

    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
