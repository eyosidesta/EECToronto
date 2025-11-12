package com.example.EECToronto.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String username, @RequestParam String newPassword) {
        return authService.changePassword(username, newPassword);
    }

}
