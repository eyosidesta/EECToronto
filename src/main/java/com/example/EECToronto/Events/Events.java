package com.example.EECToronto.Events;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
public class Events {
    @Id
    @SequenceGenerator(
            name="event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )
    private Long id;
    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "event_image_url")
    private String eventImageUrl;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "event_city")
    private String eventCity;

    @Column(name = "event_street")
    private String eventStreet;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    public Events() {}

    public Events (String eventTitle, String eventDescription, String eventImageUrl, String eventStreet, String eventCity, String eventType, LocalDateTime eventDate) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventImageUrl = eventImageUrl;
        this.eventStreet = eventStreet;
        this.eventCity = eventCity;
        this.eventType = eventType;
        this.eventDate = eventDate;
    }

    public Long getId() {
        return id;
    }
    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String event_title) {
        this.eventTitle = event_title;
    }
    public String getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public String getEventStreet() {
        return eventStreet;
    }
    public void setEventStreet(String eventStreet) {
        this.eventStreet = eventStreet;
    }
    public String getEventCity() {
        return eventCity;
    }
    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public String getEventImageUrl() {
        return eventImageUrl;
    }
    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }
    public LocalDateTime getEventDate() {
        return eventDate;
    }
    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }
}
