package com.example.EECToronto.NewMemberRequest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "new_member_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMemberRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String gender;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true) // Email is optional
    private String email;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private boolean contacted = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate = new Date();
}


