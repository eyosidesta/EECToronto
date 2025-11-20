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

    public TeamSMS() {};

    public TeamSMS(String message) {
        this.message = message;

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
