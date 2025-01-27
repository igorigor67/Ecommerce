package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    boolean existsByContactNumber(String contactNumber);
    boolean existsByUsername(String username);

    // Returns User or Null if not found
    // Enables .orElseThrow()
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
