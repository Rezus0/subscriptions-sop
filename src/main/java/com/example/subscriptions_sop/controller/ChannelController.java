package com.example.subscriptions_sop.controller;

import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ChannelController {
    private ChannelService channelService;

    @GetMapping("/{username}/channel")
    public ResponseEntity<ChannelRepresentation> getChannel(@PathVariable String username) {
        ChannelRepresentation representation = channelService.getChannel(username);
        return ResponseEntity.ok(representation);
    }

    @Autowired
    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }
}
