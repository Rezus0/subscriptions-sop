package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.dto.SubUpgradeDto;
import com.example.subscriptions_sop.model.Subscription;
import com.example.subscriptions_sop.repository.SubscriptionRepository;
import com.example.subscriptions_sop.representation_model.SubscriptionRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionRepository subscriptionRepository;
    private final ModelMapper modelMapper;

    @Value("${app.url.base}")
    private String urlBase;

    public SubscriptionServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CollectionModel<SubscriptionRepresentation> getSubscriptionsHistory(String subscriberUsername,
                                                                        String targetChannelUsername) {
//        List<Subscription> subscriptions = subscriptionRepository.findBySubscriberUsernameAndTargetChannelOwnerUsername(
//                subscriberUsername, targetChannelUsername
//        );
//        List<SubscriptionRepresentation> representations = subscriptions.forEach(subscription -> {
//            SubscriptionRepresentation representation = modelMapper.map(subscription, SubscriptionRepresentation.class);
//            representation.add(Link.of(urlBase + ""));
//        });
        return null;
    }

    @Override
    public CollectionModel<SubscriptionRepresentation> getSubscriptionsForUser(String username) {
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriberUsername(username);
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionsToRepresentations(subscriptions);
        collectionModel.add(Link.of(urlBase + username + "/subscriptions").withSelfRel())
                .add(Link.of(urlBase + username).withRel("user"));
        return collectionModel;
    }

    @Override
    public CollectionModel<SubscriptionRepresentation> getSubscriptionsForChannel(String targetChannelUsername) {
        List<Subscription> subscriptions = subscriptionRepository.findByTargetChannelOwnerUsername(targetChannelUsername);
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionsToRepresentations(subscriptions);
        collectionModel.add(Link.of(urlBase + targetChannelUsername + "/channel/subscriptions").withSelfRel())
                .add(Link.of(urlBase + targetChannelUsername + "/channel").withRel("channel"));
        return collectionModel;
    }

    @Override
    public SubscriptionRepresentation subscribe(SubDto subDto) {
        return null;
    }

    @Override
    public SubscriptionRepresentation extendSubscription(SubExtendDto subExtendDto) {
        return null;
    }

    @Override
    public SubscriptionRepresentation cancelSubscription(String subscriberUsername, String targetChannelUsername) {
        return null;
    }

    @Override
    public SubscriptionRepresentation upgradeTier(SubUpgradeDto subUpgradeDto) {
        return null;
    }

    private CollectionModel<SubscriptionRepresentation> subscriptionsToRepresentations(List<Subscription> subscriptions) {
        List<SubscriptionRepresentation> representations = subscriptions.stream().map(subscription -> {
            SubscriptionRepresentation representation = modelMapper.map(subscription, SubscriptionRepresentation.class);
            String subscriberUsername = subscription.getSubscriber().getUsername();
            String targetChannelUsername = subscription.getTargetChannel().getOwner().getUsername();
            representation.add(Link.of(urlBase + "subscription?user=" + subscriberUsername +
                            "&channel=" + targetChannelUsername).withSelfRel())
                    .add(Link.of(urlBase + subscriberUsername).withRel("user"))
                    .add(Link.of(urlBase + targetChannelUsername + "/channel").withRel("channel"));
            return representation;
        }).toList();
        return CollectionModel.of(representations);
    }

    @Autowired
    public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }
}
