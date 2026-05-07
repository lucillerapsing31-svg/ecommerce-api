package com.ws181.gepollo_rapsing.ecommerceapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security Configuration Class
 * Defines all security rules, authentication settings, CORS and access permissions
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public SecurityConfig() {
    }

    /**
     * Password Encoder Bean
     * Uses BCrypt algorithm to securely hash user passwords before storing them
     * Never stores plain text passwords for security
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Manager Bean
     * Handles authentication process, manages user login verification
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Main Security Filter Chain
     * Defines all security rules and behavior of the application
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Enable CORS configuration to allow requests from frontend and tools like Postman
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Disable CSRF protection (disabled for development/testing purposes)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Define access permissions for endpoints
            .authorizeHttpRequests(auth -> auth
                // PUBLIC ENDPOINTS: Can be accessed without login
                // All pages, registration, products and orders are open to everyone
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/login.html",
                    "/register.html",
                    "/api/v1/auth/register",
                    "/api/v1/auth/login",
                    "/api/v1/products",
                    "/api/v1/orders",
                    "/products.html",
                    "/landing.html",
                    "/account.html",
                    "/cart.html",
                    "/checkout.html"
                ).permitAll()
                
                // 👇 PROTECTED ENDPOINTS: All other paths require user to be logged in
                .anyRequest().authenticated()
            )
            
            // Login configuration
            .formLogin(form -> form
                // Path to custom login page
                .loginPage("/login.html")
                // Path where login form data is sent for processing
                .loginProcessingUrl("/login")
                // Use email field as username
                .usernameParameter("email")
                // Use password field as password
                .passwordParameter("password")
                // Redirect to products page after successful login
                .defaultSuccessUrl("/products.html", true)
                // Allow everyone to access login page
                .permitAll()
            )
            
            // Logout configuration
            .logout(logout -> logout
                // Path to trigger logout action
                .logoutUrl("/logout")
                // Redirect to login page after logout
                .logoutSuccessUrl("/login.html?logout")
                // Allow everyone to logout
                .permitAll()
            );

        return http.build();
    }

    /**
     * CORS Configuration Source
     * Defines which domains and methods are allowed to access the API
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allowed origins (domains) that can send requests to the API
        // Includes frontend development ports, backend port and allows all for testing
        configuration.setAllowedOrigins(List.of(
            "http://127.0.0.1:5500",
            "http://localhost:5500",
            "http://127.0.0.1:8080",
            "http://localhost:8080",
            "*" // Allow all origins for development/testing
        )); 
        
        // Allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Allowed headers in requests
        configuration.setAllowedHeaders(List.of("*"));
        
        // Allow sending credentials (cookies/session) with requests
        configuration.setAllowCredentials(true);
        
        // Cache time for CORS settings
        configuration.setMaxAge(3600L);
        
        // Apply CORS settings to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}