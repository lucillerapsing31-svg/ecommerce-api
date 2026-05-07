package com.ws181.gepollo_rapsing.ecommerceapi.model;

<<<<<<< HEAD
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
=======
import jakarta.persistence.*; //MUST BE jakarta NOT javax
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5

@Entity
@Table(name = "users")
public class User implements UserDetails {

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
<<<<<<< HEAD

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    // Getter and Setter for Role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Security Methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Use the role from database instead of hardcoded
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
=======
}
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
