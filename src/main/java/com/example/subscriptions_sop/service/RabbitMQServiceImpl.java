package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.model.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    private RabbitTemplate rabbitTemplate;
    private ChannelService channelService;

    @Value("${rabbitmq.exchange.golive.name}")
    private String exchangeName;

    @Value("${rabbitmq.golive.key}")
    private String routingKey;

    @Override
    public void sendGoLiveMessageForChannelSubscribers(String username) {
        List<User> subscribers = channelService.getSubscribersWithUsersOutput(username);
        subscribers.forEach(subscriber ->
            rabbitTemplate.convertAndSend(
                    exchangeName, routingKey,
                    String.format("%s just went live! \n Message for: %s \n %s",
                            username, subscriber.getUsername(), LocalDateTime.now()
                    )
            )
        );
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }
}
