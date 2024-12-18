package com.example.subscriptions_sop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubExtendDto {
    private String subscriberUsername;
    private String targetChannelUsername;
    private int durationInMonths;

    public SubExtendDto(String subscriberUsername, String targetChannelUsername, int durationInMonths) {
        this.subscriberUsername = subscriberUsername;
        this.targetChannelUsername = targetChannelUsername;
        this.durationInMonths = durationInMonths;
    }

    @NotBlank(message = "Subscriber's username can't be blank")
    public String getSubscriberUsername() {
        return subscriberUsername;
    }

    @NotBlank(message = "Target channel username can't be blank")
    public String getTargetChannelUsername() {
        return targetChannelUsername;
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

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }
}
