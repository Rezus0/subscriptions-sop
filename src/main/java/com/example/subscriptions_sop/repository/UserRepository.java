package com.example.subscriptions_sop.repository;

import com.example.subscriptions_sop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
