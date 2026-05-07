package com.ws181.gepollo_rapsing.ecommerceapi.controller;

import com.ws181.gepollo_rapsing.ecommerceapi.dto.RegisterUserDto;
import com.ws181.gepollo_rapsing.ecommerceapi.model.User;
import com.ws181.gepollo_rapsing.ecommerceapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. REGISTER ENDPOINT
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
}