package com.example.market.repository;

import com.example.market.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods (if needed)
    User findByUsername(String username);
    // Other custom queries can be defined here
}

