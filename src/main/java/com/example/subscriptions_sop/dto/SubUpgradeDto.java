package com.example.subscriptions_sop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubUpgradeDto {
    private String subscriberUsername;
    private String targetChannelUsername;
    private int tier;

    @NotBlank(message = "Subscriber's username can't be blank")
    public String getSubscriberUsername() {
        return subscriberUsername;
    }

    @NotBlank(message = "Target channel username can't be blank")
    public String getTargetChannelUsername() {
        return targetChannelUsername;
    }

    @NotNull(message = "Tier can't be null")
    @Min(value = 0, message = "Tier must be integer in {0, 1, 2} where 0 - Default, 1 - Vip, 2 - Deluxe")
    @Max(value = 2, message = "Tier must be integer in {0, 1, 2} where 0 - Default, 1 - Vip, 2 - Deluxe")
    public int getTier() {
        return tier;
    }

    public void setSubscriberUsername(String subscriberUsername) {
        this.subscriberUsername = subscriberUsername;
    }

    public void setTargetChannelUsername(String targetChannelUsername) {
        this.targetChannelUsername = targetChannelUsername;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
}
