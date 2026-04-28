package com.ws181.gepollo_rapsing.ecommerceapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore; // ADD THIS IMPORT
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Product Category.
 * One Category can contain many Products (One-to-Many relationship).
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Relationship: One Category has many Products
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // CHANGED TO EAGER
    @JsonIgnore // ADD THIS LINE HERE
    private List<Product> products = new ArrayList<>();

    // Custom constructor for creating with name only
    public Category(String name) {
        this.name = name;
    }
}