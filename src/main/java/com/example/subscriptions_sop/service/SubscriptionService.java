package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.dto.SubUpgradeDto;
import com.example.subscriptions_sop.model.Subscription;
import com.example.subscriptions_sop.representation_model.SubscriptionRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public interface SubscriptionService {
    CollectionModel<SubscriptionRepresentation> getSubscriptionsHistory(String subscriberUsername,
                                                                        String targetChannelUsername, int page, int size);

    Iterable<Subscription> getSubscriptionsDataForHistory(String subscriberUsername, String targetChannelUsername);

    CollectionModel<SubscriptionRepresentation> getSubscriptionsForUser(String username, int page, int size);

    Iterable<Subscription> getSubscriptionsDataForUser(String username);

    CollectionModel<SubscriptionRepresentation> getSubscriptionsForChannel(String targetChannelUsername,
                                                                           int page, int size);

    Iterable<Subscription> getSubscriptionsDataForChannel(String targetChannelUsername);

    SubscriptionRepresentation subscribe(SubDto subDto);

    Subscription getSubscriptionDataForSubscribe(SubDto subDto);

    SubscriptionRepresentation extendSubscription(SubExtendDto subExtendDto);

    Subscription getSubscriptionDataForExtend(SubExtendDto subExtendDto);

    SubscriptionRepresentation cancelSubscription(String subscriberUsername, String targetChannelUsername);

    Subscription getSubscriptionDataForCancel(String subscriberUsername, String targetChannelUsername);

    SubscriptionRepresentation upgradeTier(SubUpgradeDto subUpgradeDto);

    Subscription getSubscriptionDataForUpgrade(SubUpgradeDto subUpgradeDto);
}
