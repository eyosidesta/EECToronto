package com.example.EECToronto.PrayerRequest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "prayer_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrayerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(nullable = false)
    private boolean contacted = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate = new Date();
}


