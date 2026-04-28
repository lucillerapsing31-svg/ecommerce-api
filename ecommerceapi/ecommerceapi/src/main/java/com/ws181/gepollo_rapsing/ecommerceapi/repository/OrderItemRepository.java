package com.ws181.gepollo_rapsing.ecommerceapi.repository;

import com.ws181.gepollo_rapsing.ecommerceapi.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}