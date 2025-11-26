package com.example.EECToronto.SermonGroup;

import com.example.EECToronto.SermonGroupRship.SermonGroupRship;
import com.example.EECToronto.DTO.SermonType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class SermonGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "sermon_type")
    private SermonType sermonType;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private Set<SermonGroupRship> sermons = new HashSet<>();

}
