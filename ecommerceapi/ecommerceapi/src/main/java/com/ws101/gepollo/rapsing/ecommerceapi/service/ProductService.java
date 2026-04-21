package com.ws101.gepollo.rapsing.ecommerceapi.service;

import com.ws101.gepollo.rapsing.ecommerceapi.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final List<Product> productList = new ArrayList<>();
    private Long nextId = 1L;
    
    public ProductService() {
        // Sample products (10+ products as required)
        productList.add(new Product(nextId++, "Wireless Headphones", "Premium noise-cancelling wireless headphones", 59.99, "Electronics", 50, null));
        productList.add(new Product(nextId++, "Smart Watch", "Fitness tracker with heart rate monitor", 129.99, "Electronics", 30, null));
        productList.add(new Product(nextId++, "Slim Leather Wallet", "Genuine leather wallet with RFID protection", 39.99, "Accessories", 100, null));
        productList.add(new Product(nextId++, "Ceramic Travel Mug", "Double-wall insulated ceramic mug", 24.99, "Kitchen", 75, null));
        productList.add(new Product(nextId++, "LED Desk Lamp", "Adjustable brightness desk lamp", 59.99, "Electronics", 40, null));
        productList.add(new Product(nextId++, "Bluetooth Speaker", "Portable waterproof speaker", 45.00, "Electronics", 60, null));
        productList.add(new Product(nextId++, "Cotton T-Shirt", "Soft cotton t-shirt", 19.99, "Clothing", 200, null));
        productList.add(new Product(nextId++, "JavaScript Guide", "Complete guide to JavaScript", 34.99, "Books", 45, null));
        productList.add(new Product(nextId++, "Phone Case", "Shockproof phone case", 19.99, "Accessories", 150, null));
        productList.add(new Product(nextId++, "Wireless Earbuds", "True wireless earbuds", 59.99, "Electronics", 55, null));
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }
    
    public Product getProductById(Long id) {
        return productList.stream()
            .filter(product -> product.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }
    
    public Product createProduct(Product product) {
        product.setId(nextId++);
        productList.add(product);
        return product;
    }
    
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        return existingProduct;
    }
    
    public Product patchProduct(Long id, Product productPatch) {
        Product existingProduct = getProductById(id);
        if (productPatch.getName() != null) existingProduct.setName(productPatch.getName());
        if (productPatch.getDescription() != null) existingProduct.setDescription(productPatch.getDescription());
        if (productPatch.getPrice() > 0) existingProduct.setPrice(productPatch.getPrice());
        if (productPatch.getCategory() != null) existingProduct.setCategory(productPatch.getCategory());
        if (productPatch.getStockQuantity() >= 0) existingProduct.setStockQuantity(productPatch.getStockQuantity());
        if (productPatch.getImageUrl() != null) existingProduct.setImageUrl(productPatch.getImageUrl());
        return existingProduct;
    }
    
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productList.remove(product);
    }
    
    public List<Product> filterProducts(String filterType, String filterValue) {
        switch (filterType.toLowerCase()) {
            case "category":
                return productList.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(filterValue))
                    .collect(Collectors.toList());
            case "price":
                double maxPrice = Double.parseDouble(filterValue);
                return productList.stream()
                    .filter(p -> p.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
            case "name":
                return productList.stream()
                    .filter(p -> p.getName().toLowerCase().contains(filterValue.toLowerCase()))
                    .collect(Collectors.toList());
            default:
                return new ArrayList<>(productList);
        }
    }
}