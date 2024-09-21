package com.example.subscriptions_sop.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SubscriptionTier {
    DEFAULT(0), VIP(1), DELUXE(2);

    private final int value;

    SubscriptionTier(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
