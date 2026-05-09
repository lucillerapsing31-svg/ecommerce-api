package com.ws181.gepollo_rapsing.ecommerceapi.controller;

import com.ws181.gepollo_rapsing.ecommerceapi.JwtUtil;
import com.ws181.gepollo_rapsing.ecommerceapi.dto.RegisterUserDto;
import com.ws181.gepollo_rapsing.ecommerceapi.model.User;
import com.ws181.gepollo_rapsing.ecommerceapi.service.CustomUserDetailsService;
import com.ws181.gepollo_rapsing.ecommerceapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    // ✅ Full constructor injection
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // ==========================================
    // ✅ YOUR ORIGINAL REGISTER ENDPOINT
    // ==========================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto dto) {
        
        // Check if email already exists
        if(userRepository.findByEmail(dto.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }

        // Create User object from DTO
        User user = new User();
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // Set default role if not provided
        if(dto.getRole() == null || dto.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        } else {
            user.setRole(dto.getRole());
        }

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // ==========================================
    // ✅ NEW: LOGIN ENDPOINT (Generates JWT Token)
    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        try {
            // 1. Authenticate email & password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // 2. Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 3. Generate JWT token
        String jwtToken = jwtUtil.generateToken(userDetails);

        // 4. Return token to client
        return ResponseEntity.ok(Map.of(
                "token", jwtToken,
                "email", email,
                "message", "Login successful"
        ));
    }
}