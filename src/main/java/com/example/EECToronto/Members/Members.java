package com.example.EECToronto.Members;

import com.example.EECToronto.IndividualSMS.IndividualSMS;
import com.example.EECToronto.Neighborhood.Neighborhood;
import com.example.EECToronto.Status.Status;
import com.example.EECToronto.TeamMembers.TeamMembers;
import com.example.EECToronto.TeamSMS.TeamSMS;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "members")
public class Members {
    @Id
    @SequenceGenerator(
            name = "member_sequence",
            sequenceName = "member_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    private String gender;
    @Column(unique = true, nullable = false)
    private String phone;
    @Column(unique = true)
    private String email;
    private Date date_of_birth;
    @ManyToOne
    @JoinColumn(name="member_neighborhood_id", foreignKey = @ForeignKey(name="member_neighborhood_fkey"), nullable = true)
    private Neighborhood neighborhood;
    private String address;
    @ManyToOne
    @JoinColumn(name="member_status_id", foreignKey = @ForeignKey(name="member_status_fkey"), nullable = true)
    private Status status;
    @OneToOne(mappedBy = "members", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private TeamSMS team_sms;

    @OneToOne(mappedBy = "members", cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private IndividualSMS individual_sms;

    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMembers> team_members;

    public Members() {}
    public Members(String name, String gender, String phone, String email, Date date_of_birth, String address, Neighborhood neighborhood, Status status) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.status = status;
        this.neighborhood = neighborhood;
    }

    public Long getId() {
        return id;
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }
    public void setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
