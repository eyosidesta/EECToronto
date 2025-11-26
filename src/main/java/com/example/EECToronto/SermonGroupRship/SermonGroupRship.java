package com.example.EECToronto.SermonGroupRship;

import com.example.EECToronto.SermonGroup.SermonGroup;
import com.example.EECToronto.Sermons.Sermons;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "sermon_group_rship",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"sermon_id", "group_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class SermonGroupRship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sermon_id", nullable = false)
    private Sermons sermons;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
//    @JsonBackReference
    private SermonGroup group;
}
