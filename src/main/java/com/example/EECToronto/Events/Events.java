package com.example.EECToronto.Events;

import jakarta.persistence.*;

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
    private String event_title;
    private String event_imageUrl;
    private String event_description;
    private String event_place;
    private Date event_date;

    public Events() {}

    public Events (String event_title, String event_description, String event_imageUrl, String event_place, Date event_date) {
        this.event_title = event_title;
        this.event_description = event_description;
        this.event_imageUrl = event_imageUrl;
        this.event_place = event_place;
        this.event_date = event_date;

    }

    public Long getId() {
        return id;
    }
    public String getEvent_title() {
        return event_title;
    }
    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }
    public String getEvent_description() {
        return event_description;
    }
    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }
    public String getEvent_place() {
        return event_place;
    }
    public void setEvent_place(String event_place) {
        this.event_place = event_place;
    }
    public String getEvent_imageUrl() {
        return event_imageUrl;
    }
    public void setEvent_imageUrl(String event_imageUrl) {
        this.event_imageUrl = event_imageUrl;
    }
    public Date getEvent_date() {
        return event_date;
    }
    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }
}
