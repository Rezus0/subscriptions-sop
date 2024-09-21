package com.example.subscriptions_sop.representation_model;

import com.example.subscriptions_sop.model.SubscriptionTier;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class SubscriptionRepresentation extends RepresentationModel<SubscriptionRepresentation> {
    private SubscriptionTier tier;
    private LocalDateTime subscriptionStartTime;
    private LocalDateTime subscriptionEndTime;
    private boolean isActive;
    private Double price;

    public SubscriptionRepresentation() {
    }

    public SubscriptionTier getTier() {
        return tier;
    }

    public LocalDateTime getSubscriptionStartTime() {
        return subscriptionStartTime;
    }

    public LocalDateTime getSubscriptionEndTime() {
        return subscriptionEndTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public Double getPrice() {
        return price;
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
