package com.example.subscriptions_sop.controller;

import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.service.ChannelService;
import com.example.subscriptions_sop.service.RabbitMQService;
import com.example.subscriptions_sop.service.RabbitMQServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class ChannelController {
    private ChannelService channelService;
    private RabbitMQService rabbitMQService;

    @GetMapping("/{username}/channel")
    public ResponseEntity<ChannelRepresentation> getChannel(@PathVariable String username) {
        ChannelRepresentation representation = channelService.getChannel(username);
        return ResponseEntity.ok(representation);
    }

    @GetMapping("/{username}/channel/live")
    public ResponseEntity<ChannelRepresentation> goLive(@PathVariable("username") String username) {
        ChannelRepresentation representation = channelService.goLive(username);
        rabbitMQService.sendGoLiveMessageForChannelSubscribers(username);
        return ResponseEntity.ok(representation);
    }

    @GetMapping("/{username}/channel/offline")
    public ResponseEntity<ChannelRepresentation> goOffline(@PathVariable("username") String username) {
        ChannelRepresentation representation = channelService.goOffline(username);
        return ResponseEntity.ok(representation);
    }

    @Autowired
    public void setRabbitMQService(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    @Autowired
    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }
}
