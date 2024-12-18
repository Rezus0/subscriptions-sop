package com.example.subscriptions_sop.service;

public interface RabbitMQService {
    void sendGoLiveMessageForChannelSubscribers(String username);
}
