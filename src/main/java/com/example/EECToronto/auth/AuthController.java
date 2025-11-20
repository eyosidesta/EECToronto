package com.example.EECToronto.auth;

import com.example.EECToronto.Admin.Admin;
import com.example.EECToronto.DTO.AuthRequest;
import com.example.EECToronto.DTO.AuthResponse;
import com.example.EECToronto.DTO.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }
    @GetMapping("/admins")
    public List<Admin> allAdmins() {
        return authService.allAdmin();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request,
                                           @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(authService.register(request, username));
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String username, @RequestParam String newPassword) {
        return authService.changePassword(username, newPassword);
    }

}
