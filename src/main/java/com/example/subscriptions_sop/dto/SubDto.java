package com.example.subscriptions_sop.dto;

import jakarta.validation.constraints.*;

public class SubDto {
    private String subscriberUsername;
    private String targetChannelUsername;
    private int tier;
    private int durationInMonths;

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

    @NotNull(message = "Duration in months can't be null")
    @Min(value = 1, message = "Duration in months must be an integer from 1 to 12")
    @Max(value = 12, message = "Duration in months must be an integer from 1 to 12")
    public int getDurationInMonths() {
        return durationInMonths;
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

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }
}
