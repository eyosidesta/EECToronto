package com.example.EECToronto.WorshipDepartmentRequest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "worship_department_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorshipDepartmentRequest {
    @Id
    @SequenceGenerator(
            name = "worship_request_sequence",
            sequenceName = "worship_request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "worship_request_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    private String gender;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private boolean contacted = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate = new Date();
}



