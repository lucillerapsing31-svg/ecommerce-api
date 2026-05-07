package com.ws181.gepollo_rapsing.ecommerceapi.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterUserDto {

    @NotBlank(message = "Full name is required")
    private String fullname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String role;
}