package com.example.EECToronto.auth;

import com.example.EECToronto.Admin.Admin;
import com.example.EECToronto.DTO.AuthRequest;
import com.example.EECToronto.DTO.AuthResponse;
import com.example.EECToronto.DTO.RegisterRequest;
import com.example.EECToronto.DTO.Role;
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

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestHeader("Authorization") String authHeader,
                                                @RequestParam String name) {
        String token = authHeader.substring(7);
        String currentUsername = jwtService.extractUsername(token);
        return ResponseEntity.ok(authService.updateProfile(currentUsername, name));
    }

    @PutMapping("/profile/password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String authHeader,
                                                 @RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        String token = authHeader.substring(7);
        String currentUsername = jwtService.extractUsername(token);
        return ResponseEntity.ok(authService.changePasswordWithOldPassword(currentUsername, oldPassword, newPassword));
    }

    @GetMapping("/manage/admins")
    public ResponseEntity<List<Admin>> getAllAdminsForManagement(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(authService.getAllAdminsForManagement(username));
    }

    @DeleteMapping("/manage/admins/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long adminId,
                                              @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(authService.deleteAdmin(adminId, username));
    }

    @PutMapping("/manage/admins/{adminId}/role")
    public ResponseEntity<String> changeAdminRole(@PathVariable Long adminId,
                                                  @RequestParam Role newRole,
                                                  @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(authService.changeAdminRole(adminId, newRole, username));
    }

    // Forgot Password - Public endpoint
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String username) {
        return ResponseEntity.ok(authService.requestPasswordReset(username));
    }

    // Reset Password - Public endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestParam String newPassword) {
        return ResponseEntity.ok(authService.resetPassword(token, newPassword));
    }

    // Validate Reset Token - Public endpoint
    @GetMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateResetToken(token));
    }

}
