package com.ws181.gepollo_rapsing.ecommerceapi.repository;

import com.ws181.gepollo_rapsing.ecommerceapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Finds products by category name
    List<Product> findByCategoryName(String categoryName);
    
    // Finds products by name containing keyword
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Finds products within a price range
    List<Product> findByPriceBetween(double min, double max);
}