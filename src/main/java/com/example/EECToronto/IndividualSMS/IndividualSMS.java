package com.example.EECToronto.IndividualSMS;

import com.example.EECToronto.Members.Members;
import jakarta.persistence.*;

@Entity
@Table(name = "individual_sms")
public class IndividualSMS {
    @Id
    @SequenceGenerator(
            name = "individual_sms_sequence",
            sequenceName = "individual_sms_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "individual_sms_sequence"
    )

    private Long id;
    @Column(nullable = false)
    private String message;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="author_id", foreignKey = @ForeignKey(name = "author_individual_sms_fkey"))
    private Members members;

    public IndividualSMS() {}

    public IndividualSMS(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
