package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.dto.SubUpgradeDto;
import com.example.subscriptions_sop.representation_model.SubscriptionRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public interface SubscriptionService {
    CollectionModel<SubscriptionRepresentation> getSubscriptionsHistory(String subscriberUsername,
                                                                 String targetChannelUsername);
    CollectionModel<SubscriptionRepresentation> getSubscriptionsForUser(String username);
    CollectionModel<SubscriptionRepresentation> getSubscriptionsForChannel(String targetChannelUsername);
    SubscriptionRepresentation subscribe(SubDto subDto);

    SubscriptionRepresentation extendSubscription(SubExtendDto subExtendDto);

    SubscriptionRepresentation cancelSubscription(String subscriberUsername, String targetChannelUsername);

    SubscriptionRepresentation upgradeTier(SubUpgradeDto subUpgradeDto);
}
