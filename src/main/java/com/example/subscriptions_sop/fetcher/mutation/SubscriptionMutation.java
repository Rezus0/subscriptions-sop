package com.example.subscriptions_sop.fetcher.mutation;

import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.dto.SubUpgradeDto;
import com.example.subscriptions_sop.model.Subscription;
import com.example.subscriptions_sop.service.SubscriptionService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class SubscriptionMutation {
    private SubscriptionService subscriptionService;

    @DgsMutation
    public Subscription subscribe(@InputArgument("subscription") SubDto subDto) {
        return subscriptionService.getSubscriptionDataForSubscribe(subDto);
    }

    @DgsMutation
    public Subscription extendSubscription(@InputArgument("subscription") SubExtendDto subExtendDto) {
        return subscriptionService.getSubscriptionDataForExtend(subExtendDto);
    }

    @DgsMutation
    public Subscription cancelSubscription(@InputArgument("subscriberUsername") String subscriberUsername,
                                           @InputArgument("targetChannelUsername") String targetChannelUsername) {
        return subscriptionService.getSubscriptionDataForCancel(subscriberUsername, targetChannelUsername);
    }

    @DgsMutation
    public Subscription upgradeTier(@InputArgument("subscription") SubUpgradeDto subUpgradeDto) {
        return subscriptionService.getSubscriptionDataForUpgrade(subUpgradeDto);
    }

    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}
