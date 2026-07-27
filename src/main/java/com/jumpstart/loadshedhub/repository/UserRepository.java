package com.jumpstart.loadshedhub.repository;

import com.jumpstart.loadshedhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//handles all DB operations for user entity
public interface UserRepository extends JpaRepository<User, Long> {
    //check if user exists when they try to log in
    Optional<User> findByEmail(String email);
    long countByRole(com.jumpstart.loadshedhub.entity.Role role);
}
