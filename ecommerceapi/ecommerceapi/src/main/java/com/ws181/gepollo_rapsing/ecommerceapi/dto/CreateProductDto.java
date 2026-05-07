package com.ws181.gepollo_rapsing.ecommerceapi.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProductDto {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String imageUrl;
}