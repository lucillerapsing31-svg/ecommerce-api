package com.ws181.gepollo_rapsing.ecommerceapi.repository;

import com.ws181.gepollo_rapsing.ecommerceapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}