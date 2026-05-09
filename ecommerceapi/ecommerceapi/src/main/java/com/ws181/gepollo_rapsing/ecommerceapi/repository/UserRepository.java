package com.ws181.gepollo_rapsing.ecommerceapi.repository;

import com.ws181.gepollo_rapsing.ecommerceapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}