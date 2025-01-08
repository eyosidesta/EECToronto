package com.example.EECToronto.Email;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class EmailModel {
    @Id
    @SequenceGenerator(
            name = "email_sequence",
            sequenceName = "email_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "email_sequence"
    )
    private Long id;
    private String sender;
    private List<String> receiver;
    private String subject;
    private String emailMessage;

    public EmailModel(){}

    public EmailModel(String sender, List<String> receiver, String subject, String emailMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.emailMessage = emailMessage;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public List<String> receiver() {
        return receiver;
    }
    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getEmailMessage() {
        return emailMessage;
    }
    public void setEmailMessage() {
        this.emailMessage = emailMessage;
    }
}
