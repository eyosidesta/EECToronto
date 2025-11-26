package com.example.EECToronto.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SermonGroupRequestDTO {
    private String title;  // Maps to name in entity
    private String explanation;  // Maps to description in entity
    private String type;  // Maps to sermonType enum (ENGLISH/AMHARIC)
    private MultipartFile image;
}


