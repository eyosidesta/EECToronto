package com.example.EECToronto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/prayer-department-requests").permitAll() // POST only - public user submissions
                        .requestMatchers("/api/worship-department-requests").permitAll() // POST only - public user submissions
                        .requestMatchers("/api/family-department-requests").permitAll() // POST only - public user submissions
                        .requestMatchers("/api/evangelism-department-requests").permitAll() // POST only - public user submissions
                        .requestMatchers("/api/sermons/**").permitAll() // GET only - public sermon access
                        .requestMatchers("/api/sermon-groups/**").permitAll() // GET only - public sermon group access
                        .requestMatchers("/api/events/**").permitAll() // GET only - public event access
                        
                        // Admin registration - only SUPER_ADMIN and MASTER_ADMIN
                        .requestMatchers("/api/auth/register").hasAnyRole("SUPER_ADMIN", "MASTER_ADMIN")
                        
                        // All admin endpoints grouped under /api/admin/**
                        .requestMatchers("/api/admin/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MASTER_ADMIN")
                        
                        // Super admin endpoints - only SUPER_ADMIN and MASTER_ADMIN
                        .requestMatchers("/api/super-admin/**").hasAnyRole("SUPER_ADMIN", "MASTER_ADMIN")
                        
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "https://www.geecvancouver.ca",
                "http://localhost:3036"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
