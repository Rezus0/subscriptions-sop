package com.example.subscriptions_sop.repository;

import com.example.subscriptions_sop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    @Modifying
    @Query("UPDATE User u SET u.balance = u.balance + :amount WHERE u.username = :username")
    void deposit(@Param("username") String username, @Param("amount") Double amount);
}
