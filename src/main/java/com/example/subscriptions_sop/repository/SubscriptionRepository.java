package com.example.subscriptions_sop.repository;

import com.example.subscriptions_sop.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findBySubscriberUsernameAndTargetChannelOwnerUsername(String subscriberUsername,
                                                                             String targetChannelUsername);
    List<Subscription> findBySubscriberUsername(String subscriberUsername);
    List<Subscription> findByTargetChannelOwnerUsername(String targetChannelUsername);
}
