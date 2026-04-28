package com.ws181.gepollo_rapsing.ecommerceapi.repository;

import com.ws181.gepollo_rapsing.ecommerceapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}