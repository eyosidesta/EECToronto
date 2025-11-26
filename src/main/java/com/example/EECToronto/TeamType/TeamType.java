package com.example.EECToronto.TeamType;

import com.example.EECToronto.Teams.Teams;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="team_type")
public class TeamType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    private String description;

    public TeamType() {};

    public TeamType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
