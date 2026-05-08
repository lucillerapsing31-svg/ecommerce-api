package com.ws181.gepollo_rapsing.ecommerceapi.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class User implements UserDetails { // Implement UserDetails here

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String email;
    private String password;
    private String confirmPassword;
    
    // Add role field
    private String role;

    public User() {}

    public User(String fullname, String email, String password, String role) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // IMPORTANT: Spring Security expects this getter to return the actual password.
    // If you have a separate field for `confirmPassword`, that's fine,
    // but getPassword() should return the hashed password used for authentication.
    @Override // This annotation indicates it's an override from UserDetails
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    // Getter and Setter for Role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Security Methods (from UserDetails interface)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert the role string into a GrantedAuthority
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getUsername() {
        return this.email; // The email is used as the username for authentication
    }

    @Override
    public boolean isAccountNonExpired() { return true; } // You might implement logic here later

    @Override
    public boolean isAccountNonLocked() { return true; } // You might implement logic here later

    @Override
    public boolean isCredentialsNonExpired() { return true; } // You might implement logic here later

    @Override
    public boolean isEnabled() { return true; } // You might implement logic here later
}