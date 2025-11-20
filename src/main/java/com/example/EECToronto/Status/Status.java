package com.example.EECToronto.Status;

import com.example.EECToronto.Members.Members;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="status")
public class Status {
    @Id
    @SequenceGenerator(
            name="status_sequence",
            sequenceName = "status_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "status_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;

    public Status() {};
    public Status(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

}
