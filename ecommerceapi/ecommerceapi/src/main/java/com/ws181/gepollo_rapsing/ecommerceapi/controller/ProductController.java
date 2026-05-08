package com.ws181.gepollo_rapsing.ecommerceapi.controller;

import com.ws181.gepollo_rapsing.ecommerceapi.dto.CreateProductDto;
import com.ws181.gepollo_rapsing.ecommerceapi.dto.ProductListingEntry;
import com.ws181.gepollo_rapsing.ecommerceapi.model.Product;
import com.ws181.gepollo_rapsing.ecommerceapi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
<<<<<<< HEAD
@CrossOrigin(origins = "*")
=======
@CrossOrigin(origins = "*") // ADD THIS LINE HERE
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ FIXED: GET all products - Returns FULL product objects
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // GET single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET filter products
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {

        List<Product> result;

        switch (filterType.toLowerCase()) {
            case "category":
                result = productService.filterByCategory(filterValue);
                break;
            case "name":
                result = productService.filterByName(filterValue);
                break;
            case "price":
                String[] range = filterValue.split(",");
                double min = Double.parseDouble(range[0]);
                double max = range.length > 1 ? Double.parseDouble(range[1]) : Double.MAX_VALUE;
                result = productService.filterByPrice(min, max);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    // POST create new product
    @PostMapping
    public ResponseEntity<ProductListingEntry> createProduct(@Valid @RequestBody CreateProductDto dto) {
        Product created = productService.createProduct(dto);
        ProductListingEntry response = new ProductListingEntry(
                created.getId(),
                created.getName(),
                created.getPrice()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT update entire product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH partially update product
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partialUpdateProduct(@PathVariable Long id, @RequestBody Product partialProduct) {
        return productService.getProductById(id)
                .map(existing -> {
                    if (partialProduct.getName() != null) existing.setName(partialProduct.getName());
                    if (partialProduct.getDescription() != null) existing.setDescription(partialProduct.getDescription());
                    if (partialProduct.getPrice() != 0) existing.setPrice(partialProduct.getPrice());
                    if (partialProduct.getCategory() != null) existing.setCategory(partialProduct.getCategory());
                    if (partialProduct.getStockQuantity() != 0) existing.setStockQuantity(partialProduct.getStockQuantity());
                    if (partialProduct.getImageUrl() != null) existing.setImageUrl(partialProduct.getImageUrl());

                    return productService.updateProduct(id, existing).orElse(null);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
