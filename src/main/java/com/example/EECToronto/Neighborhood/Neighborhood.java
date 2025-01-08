package com.example.EECToronto.Neighborhood;

import com.example.EECToronto.Members.Members;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "neighborhood")
public class Neighborhood {
    @Id
    @SequenceGenerator(
            name="neighborhood_sequence",
            sequenceName = "neighborhood_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "neighborhood_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @OneToMany(mappedBy = "neighborhood", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Members> members = new ArrayList<>();

    public Neighborhood() {};

    public Neighborhood(String name, String description) {
        this.name = name;
        this.description = name;
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
