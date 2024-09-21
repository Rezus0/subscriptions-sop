package com.example.subscriptions_sop.repository;

import com.example.subscriptions_sop.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

}
