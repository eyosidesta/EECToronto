package com.example.EECToronto.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private String role;
    private boolean passwordChanged;
    private String name;
}
