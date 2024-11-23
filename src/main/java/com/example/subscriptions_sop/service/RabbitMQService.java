package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.model.Channel;

public interface RabbitMQService {
    void sendGoLiveMessageForChannelSubscribers(String username);
}
