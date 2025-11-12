package com.example.EECToronto.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class EventRequestDTO {
    private String eventTitle;
    private String eventDescription;
    private String eventCity;
    private String eventStreet;
    private String eventType;
    private LocalDateTime eventDate;
    private MultipartFile eventImage;
}
