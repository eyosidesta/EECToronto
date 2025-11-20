package com.example.EECToronto.auth;

import com.example.EECToronto.Admin.Admin;
import com.example.EECToronto.Admin.AdminRepository;
import com.example.EECToronto.DTO.AuthRequest;
import com.example.EECToronto.DTO.AuthResponse;
import com.example.EECToronto.DTO.RegisterRequest;
import com.example.EECToronto.DTO.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(AuthenticationManager authenticationManager,
                       AdminRepository adminRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public List<Admin> allAdmin() {
        return adminRepository.findAll();

    }

    public AuthResponse login(AuthRequest request) {


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        if (!admin.isPasswordChanged()) {
            // client should call change-password endpoint first
            String token = jwtService.generateToken(admin.getUsername(), admin.getRole().name());
            return new AuthResponse(token, "Please change your default password first", admin.getRole().name(), admin.isPasswordChanged());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(admin.getUsername(), admin.getRole().name());
        return new AuthResponse(token, "Login Successful", admin.getRole().name(), admin.isPasswordChanged());
    }

    public String register(RegisterRequest request, String currentAdminUsername) {
        var currentAdmin = adminRepository.findByUsername(currentAdminUsername)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        if (currentAdmin.getRole() != Role.SUPER_ADMIN) {
            throw  new RuntimeException("Only Super Admin can register new Admins.");
        }
        if (adminRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already existed");
        }

        Admin newAdmin = new Admin();
        newAdmin.setName(request.getName());
        newAdmin.setUsername(request.getUsername());
        newAdmin.setRole(request.getRole());
        newAdmin.setPassword(passwordEncoder.encode("123456"));
        newAdmin.setPasswordChanged(false);

        adminRepository.save(newAdmin);
        return "Admin Registration Successful!";
    }

    public String changePassword(String username, String newPassword) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setPasswordChanged(true);
        adminRepository.save(admin);

        return "Password Changed Successfully!";
    }
}
