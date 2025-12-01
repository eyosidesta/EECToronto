package com.example.EECToronto.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String gender;
    private LocalDate joinedDate;
}



