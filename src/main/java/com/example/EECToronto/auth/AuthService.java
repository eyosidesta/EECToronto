package com.example.EECToronto.auth;

import com.example.EECToronto.Admin.AdminRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       AdminRepository adminRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {
        var admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        if (!admin.isPasswordChanged()) {
            return new AuthResponse(null, "Please Change your default password first", admin.getRole().name());
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(admin.getUsername(), admin.getRole().name());

        return new AuthResponse(token, "Login Successful", admin.getRole().name());
    }

    public String changePassword(String username, String newPassword) {
        var admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setPasswordChanged(true);
        adminRepository.save(admin);

        return "Password Changed Successfully!";
    }
}
