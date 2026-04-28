package com.ws181.gepollo_rapsing.ecommerceapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private int stockQuantity;
    private String imageUrl; // optional
} 