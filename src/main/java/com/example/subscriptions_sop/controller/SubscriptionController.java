package com.example.subscriptions_sop.controller;

import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.dto.SubUpgradeDto;
import com.example.subscriptions_sop.representation_model.SubscriptionRepresentation;
import com.example.subscriptions_sop.service.SubscriptionService;
import org.modelmapper.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/")
public class SubscriptionController {
    private SubscriptionService subscriptionService;
    @Value("${app.url.base}")
    private String urlBase;

    @GetMapping("/subscription")
    public ResponseEntity<CollectionModel<SubscriptionRepresentation>> getSubscriptionHistory(
            @RequestParam("user") String subscriberUsername,
            @RequestParam("channel") String targetChannelUsername,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionService.getSubscriptionsHistory(
                subscriberUsername, targetChannelUsername, page, size
        );
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{username}/subscriptions")
    public ResponseEntity<CollectionModel<SubscriptionRepresentation>> getSubscriptionsForUser(
            @PathVariable("username") String username,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionService.getSubscriptionsForUser(
                username, page, size
        );
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{targetChannelUsername}/channel/subscriptions")
    public ResponseEntity<CollectionModel<SubscriptionRepresentation>> getSubscriptionsForChannel(
            @PathVariable("targetChannelUsername") String targetChannelUsername,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionService.getSubscriptionsForChannel(
                targetChannelUsername, page, size
        );
        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping("/subscription/subscribe")
    public ResponseEntity<SubscriptionRepresentation> subscribe(@RequestBody SubDto subDto) {
        SubscriptionRepresentation representation = subscriptionService.subscribe(subDto);
        return ResponseEntity.created(URI.create(urlBase + "subscription?user=" + subDto.getSubscriberUsername() +
                "&channel=" + subDto.getTargetChannelUsername())).body(representation);
    }

    @PatchMapping("/subscription/extend")
    public ResponseEntity<SubscriptionRepresentation> extendSubscription(@RequestBody SubExtendDto subExtendDto) {
        SubscriptionRepresentation representation = subscriptionService.extendSubscription(subExtendDto);
        return ResponseEntity.ok(representation);
    }

    @DeleteMapping("/subscription/cancel")
    public ResponseEntity<SubscriptionRepresentation> cancelSubscription(
            @RequestParam("user") String username, @RequestParam("channel") String channel
    ) {
        SubscriptionRepresentation representation = subscriptionService.cancelSubscription(username, channel);
        return ResponseEntity.ok(representation);
    }

    @PatchMapping("/subscription/upgrade")
    public ResponseEntity<SubscriptionRepresentation> upgradeTier(@RequestBody SubUpgradeDto subUpgradeDto) {
        SubscriptionRepresentation representation = subscriptionService.upgradeTier(subUpgradeDto);
        return ResponseEntity.ok(representation);
    }


    @Autowired
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}
