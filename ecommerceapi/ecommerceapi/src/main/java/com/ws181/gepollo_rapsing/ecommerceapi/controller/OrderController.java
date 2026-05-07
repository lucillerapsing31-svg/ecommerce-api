package com.ws181.gepollo_rapsing.ecommerceapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createOrder(@RequestBody String orderData) {
        return ResponseEntity.ok("Order created successfully");
    }
}