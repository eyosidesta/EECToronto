package com.example.EECToronto.AcceptChristRequest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "accept_christ_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptChristRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true) // Email is optional
    private String email;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private String saidYesFor; // User-selected value

    @Column(nullable = false)
    private boolean contacted = false;

    @Column(nullable = true) // Status is null initially, set to "Contacted to accept Christ" when marked as contacted
    private String status;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate = new Date();
}

