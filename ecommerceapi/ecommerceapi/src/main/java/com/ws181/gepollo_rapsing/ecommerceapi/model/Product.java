package com.ws181.gepollo_rapsing.ecommerceapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity representing a Product in the e-commerce system.
 * Each Product belongs to one Category.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String imageUrl; // optional

    // Relationship: Many Products belong to one Category
    @ManyToOne(fetch = FetchType.EAGER) // CHANGED FROM LAZY TO EAGER
    @JoinColumn(name = "category_id")
    // REMOVED @JsonIgnore HERE SO CATEGORY SHOWS UP
    private Category category;
}