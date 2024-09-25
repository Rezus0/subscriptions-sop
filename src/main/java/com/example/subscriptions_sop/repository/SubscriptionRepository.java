package com.example.subscriptions_sop.repository;

import com.example.subscriptions_sop.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Page<Subscription> findBySubscriberUsername(String subscriberUsername, Pageable pageable);

    Page<Subscription> findByTargetChannelOwnerUsername(String targetChannelUsername, Pageable pageable);

    Page<Subscription> findBySubscriberUsernameAndTargetChannelOwnerUsername(
            String subscriberUsername, String targetChannelUsername, Pageable pageable
    );

    List<Subscription> findBySubscriberUsername(String subscriberUsername);

    List<Subscription> findByTargetChannelOwnerUsername(String targetChannelUsername);

    List<Subscription> findBySubscriberUsernameAndTargetChannelOwnerUsername(String subscriberUsername,
                                                                             String targetChannelUsername);
}
