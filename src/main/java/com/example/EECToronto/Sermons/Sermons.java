package com.example.EECToronto.Sermons;

import com.example.EECToronto.DTO.SermonType;
import com.example.EECToronto.SermonGroupRship.SermonGroupRship;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Sermons {
    @Id
    @SequenceGenerator(
            name="sermon_sequence",
            sequenceName = "sermon_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sermon_sequence"
    )
    private Long id;

    @Column(name = "preacher_name", nullable = false)
    private String preacherName;

    @Column(name = "sermon_title", nullable = false)
    private String sermonTitle;

    @Column(name = "youtube_link")
    private String youtubeLink;

    @Column(name = "sermon_date", nullable = false)
    private Date sermonDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "sermon_type", nullable = false)
    private SermonType sermonType;

    @Column(name = "created_by_admin_name")
    private String createdByAdminName;

    @OneToMany(mappedBy = "sermons", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<SermonGroupRship> groups = new HashSet<>();
}
