package com.example.subscriptions_sop.fetcher.query;

import com.example.subscriptions_sop.model.Subscription;
import com.example.subscriptions_sop.service.SubscriptionService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class SubscriptionQuery {

    private SubscriptionService subscriptionService;

    @DgsQuery
    public Iterable<Subscription> subscriptionsHistory(@InputArgument(name = "subscriberUsername") String subscriberUsername,
                                             @InputArgument(name = "targetChannelUsername") String targetChannelUsername
                           ) {
        return subscriptionService.getSubscriptionsDataForHistory(subscriberUsername, targetChannelUsername);
    }

    @DgsQuery
    public Iterable<Subscription> subscriptionsForUser(@InputArgument(name = "username") String username) {
        return subscriptionService.getSubscriptionsDataForUser(username);
    }

    @DgsQuery
    public Iterable<Subscription> subscriptionsForChannel(
            @InputArgument(name = "targetChannelUsername") String targetChannelUsername
    ) {
        return subscriptionService.getSubscriptionsDataForChannel(targetChannelUsername);
    }

    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}
