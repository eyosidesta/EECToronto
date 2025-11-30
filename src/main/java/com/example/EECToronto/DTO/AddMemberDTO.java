package com.example.EECToronto.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberDTO {
    private String name;
    private String gender;
    private String phone;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
}


