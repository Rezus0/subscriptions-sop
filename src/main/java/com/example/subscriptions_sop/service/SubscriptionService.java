package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.dto.SubUpgradeDto;
import com.example.subscriptions_sop.representation_model.SubscriptionRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface SubscriptionService {
    CollectionModel<SubscriptionRepresentation> getSubscriptions(String subscriberUsername,
                                                                 String targetChannelUsername);
    EntityModel<SubscriptionRepresentation> subscribe(SubDto subDto);

    EntityModel<SubscriptionRepresentation> extendSubscription(SubExtendDto subExtendDto);

    EntityModel<SubscriptionRepresentation> cancelSubscription(String subscriberUsername, String targetChannelUsername);

    EntityModel<SubscriptionRepresentation> upgradeTier(SubUpgradeDto subUpgradeDto);
}
