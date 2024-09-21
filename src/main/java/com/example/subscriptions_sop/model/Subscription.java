package com.example.subscriptions_sop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription extends BaseEntity {

    private User subscriber;
    private Channel targetChannel;
    private SubscriptionTier tier;
    private LocalDateTime subscriptionStartTime;
    private LocalDateTime subscriptionEndTime;
    private boolean isActive = true;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    @NotNull(message = "Subscriber can't be null")
    public User getSubscriber() {
        return subscriber;
    }

    @ManyToOne
    @JoinColumn(name = "target_channel_id")
    @NotNull(message = "Target Channel can't be null")
    public Channel getTargetChannel() {
        return targetChannel;
    }

    @Enumerated
    @NotNull(message = "Tier can't be null")
    public SubscriptionTier getTier() {
        return tier;
    }

    @NotNull(message = "Subscription start time can't be null")
    public LocalDateTime getSubscriptionStartTime() {
        return subscriptionStartTime;
    }

    @NotNull(message = "Subscription end time can't be null")
    public LocalDateTime getSubscriptionEndTime() {
        return subscriptionEndTime;
    }

    @NotNull(message = "Active status can't be null")
    public boolean isActive() {
        return isActive;
    }

    @PositiveOrZero(message = "Price can't be lower than 0")
    public Double getPrice() {
        return price;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    public void setTargetChannel(Channel targetChannel) {
        this.targetChannel = targetChannel;
    }

    public void setTier(SubscriptionTier tier) {
        this.tier = tier;
    }

    public void setSubscriptionStartTime(LocalDateTime subscriptionStartTime) {
        this.subscriptionStartTime = subscriptionStartTime;
    }

    public void setSubscriptionEndTime(LocalDateTime subscriptionEndTime) {
        this.subscriptionEndTime = subscriptionEndTime;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
