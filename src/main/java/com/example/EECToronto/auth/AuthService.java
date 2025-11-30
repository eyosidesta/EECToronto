package com.example.EECToronto.auth;

import com.example.EECToronto.Admin.Admin;
import com.example.EECToronto.Admin.AdminRepository;
import com.example.EECToronto.DTO.AuthRequest;
import com.example.EECToronto.DTO.AuthResponse;
import com.example.EECToronto.DTO.RegisterRequest;
import com.example.EECToronto.DTO.Role;
import com.example.EECToronto.EmailService.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(AuthenticationManager authenticationManager,
                       AdminRepository adminRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       EmailService emailService,
                       PasswordResetTokenRepository passwordResetTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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
            return new AuthResponse(token, "Please change your default password first", admin.getRole().name(), admin.isPasswordChanged(), admin.getName());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(admin.getUsername(), admin.getRole().name());
        return new AuthResponse(token, "Login Successful", admin.getRole().name(), admin.isPasswordChanged(), admin.getName());
    }

    public String register(RegisterRequest request, String currentAdminUsername) {
        var currentAdmin = adminRepository.findByUsername(currentAdminUsername)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        
        // Only SUPER_ADMIN and MASTER_ADMIN can register
        if (currentAdmin.getRole() != Role.SUPER_ADMIN && currentAdmin.getRole() != Role.MASTER_ADMIN) {
            throw new RuntimeException("Only Super Admin or Master Admin can register new Admins.");
        }
        
        // SUPER_ADMIN cannot register MASTER_ADMIN
        if (currentAdmin.getRole() == Role.SUPER_ADMIN && request.getRole() == Role.MASTER_ADMIN) {
            throw new RuntimeException("Super Admin cannot register Master Admin.");
        }
        
        if (adminRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already existed");
        }

        // Validate email format
        String email = request.getUsername();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Invalid email format. Username must be a valid email address.");
        }

        Admin newAdmin = new Admin();
        newAdmin.setName(request.getName());
        newAdmin.setUsername(request.getUsername()); // Store email as username
        newAdmin.setRole(request.getRole());
        newAdmin.setPassword(passwordEncoder.encode("123456"));
        newAdmin.setPasswordChanged(false);

        adminRepository.save(newAdmin);
        
        // Send registration notification email
        try {
            emailService.sendRegistrationNotification(request.getUsername(), request.getUsername());
        } catch (Exception e) {
            // Log error but don't fail registration
            log.error("Failed to send registration email: " + e.getMessage());
        }
        
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

    public String updateProfile(String username, String newName) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // Update name
        if (newName != null && !newName.trim().isEmpty()) {
            admin.setName(newName);
        }

        adminRepository.save(admin);
        return "Profile Updated Successfully!";
    }

    public String changePasswordWithOldPassword(String username, String oldPassword, String newPassword) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Update password
        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setPasswordChanged(true);
        adminRepository.save(admin);

        return "Password Changed Successfully!";
    }

    public List<Admin> getAllAdminsForManagement(String currentAdminUsername) {
        var currentAdmin = adminRepository.findByUsername(currentAdminUsername)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        
        // Only SUPER_ADMIN and MASTER_ADMIN can view all admins
        if (currentAdmin.getRole() != Role.SUPER_ADMIN && currentAdmin.getRole() != Role.MASTER_ADMIN) {
            throw new RuntimeException("Unauthorized to view admins");
        }
        
        return adminRepository.findAll();
    }

    public String deleteAdmin(Long adminId, String currentAdminUsername) {
        var currentAdmin = adminRepository.findByUsername(currentAdminUsername)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        
        Admin adminToDelete = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // SUPER_ADMIN can only delete ADMIN
        if (currentAdmin.getRole() == Role.SUPER_ADMIN) {
            if (adminToDelete.getRole() == Role.SUPER_ADMIN || adminToDelete.getRole() == Role.MASTER_ADMIN) {
                throw new RuntimeException("Super Admin cannot delete Super Admin or Master Admin");
            }
        }
        
        // MASTER_ADMIN can delete ADMIN and SUPER_ADMIN, but not MASTER_ADMIN
        if (currentAdmin.getRole() == Role.MASTER_ADMIN) {
            if (adminToDelete.getRole() == Role.MASTER_ADMIN) {
                throw new RuntimeException("Master Admin cannot delete another Master Admin");
            }
        }
        
        adminRepository.delete(adminToDelete);
        return "Admin deleted successfully";
    }

    public String changeAdminRole(Long adminId, Role newRole, String currentAdminUsername) {
        var currentAdmin = adminRepository.findByUsername(currentAdminUsername)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        
        Admin adminToUpdate = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // SUPER_ADMIN can only promote ADMIN to SUPER_ADMIN (not demote SUPER_ADMIN)
        if (currentAdmin.getRole() == Role.SUPER_ADMIN) {
            if (adminToUpdate.getRole() == Role.SUPER_ADMIN) {
                throw new RuntimeException("Super Admin cannot change role of another Super Admin");
            }
            if (adminToUpdate.getRole() == Role.MASTER_ADMIN) {
                throw new RuntimeException("Super Admin cannot change role of Master Admin");
            }
            if (newRole == Role.MASTER_ADMIN) {
                throw new RuntimeException("Super Admin cannot promote to Master Admin");
            }
            // Can only promote ADMIN to SUPER_ADMIN
            if (adminToUpdate.getRole() == Role.ADMIN && newRole == Role.SUPER_ADMIN) {
                adminToUpdate.setRole(newRole);
                adminRepository.save(adminToUpdate);
                return "Admin role updated successfully";
            } else {
                throw new RuntimeException("Invalid role change");
            }
        }
        
        // MASTER_ADMIN can change role of ADMIN and SUPER_ADMIN, but not MASTER_ADMIN
        if (currentAdmin.getRole() == Role.MASTER_ADMIN) {
            if (adminToUpdate.getRole() == Role.MASTER_ADMIN) {
                throw new RuntimeException("Master Admin cannot change role of another Master Admin");
            }
            // Can promote/demote ADMIN and SUPER_ADMIN (but not to MASTER_ADMIN if target is not already MASTER_ADMIN)
            if (newRole == Role.MASTER_ADMIN && adminToUpdate.getRole() != Role.MASTER_ADMIN) {
                // Allow promoting to MASTER_ADMIN
                adminToUpdate.setRole(newRole);
                adminRepository.save(adminToUpdate);
                return "Admin role updated successfully";
            } else if (newRole != Role.MASTER_ADMIN) {
                // Allow changing to ADMIN or SUPER_ADMIN
                adminToUpdate.setRole(newRole);
                adminRepository.save(adminToUpdate);
                return "Admin role updated successfully";
            } else {
                throw new RuntimeException("Invalid role change");
            }
        }
        
        throw new RuntimeException("Unauthorized to change admin role");
    }
    
    public String requestPasswordReset(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this email address"));
        
        // Generate reset token
        String token = UUID.randomUUID().toString();
        
        // Create reset token entity
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsername(username);
        resetToken.setUsed(false);
        
        // Set expiry to 1 hour from now
        Date expiryDate = new Date(System.currentTimeMillis() + 3600000); // 1 hour
        resetToken.setExpiryDate(expiryDate);
        
        // Delete any existing unused tokens for this user
        passwordResetTokenRepository.findByUsernameAndUsedFalse(username)
                .ifPresent(existingToken -> passwordResetTokenRepository.delete(existingToken));
        
        passwordResetTokenRepository.save(resetToken);
        
        // Send password reset email
        emailService.sendPasswordResetEmail(username, token);
        
        return "Password reset email sent successfully";
    }
    
    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));
        
        // Check if token is expired
        if (resetToken.getExpiryDate().before(new Date())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Reset token has expired");
        }
        
        // Check if token is already used
        if (resetToken.isUsed()) {
            throw new RuntimeException("Reset token has already been used");
        }
        
        // Find admin and update password
        Admin admin = adminRepository.findByUsername(resetToken.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        admin.setPassword(passwordEncoder.encode(newPassword));
        admin.setPasswordChanged(true);
        adminRepository.save(admin);
        
        // Mark token as used
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        
        return "Password reset successfully";
    }
    
    public String validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));
        
        // Check if token is expired
        if (resetToken.getExpiryDate().before(new Date())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Reset token has expired");
        }
        
        return "Token is valid";
    }
}
