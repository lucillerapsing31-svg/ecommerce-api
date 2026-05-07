package com.ws181.gepollo_rapsing.ecommerceapi.controller;

import com.ws181.gepollo_rapsing.ecommerceapi.model.User;
import com.ws181.gepollo_rapsing.ecommerceapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Creating user is allowed for everyone (Registration)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        
        // ENCRYPT PASSWORD BEFORE SAVING
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}