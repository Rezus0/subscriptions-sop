package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.controller.ChannelController;
import com.example.subscriptions_sop.controller.SubscriptionController;
import com.example.subscriptions_sop.controller.UserController;
import com.example.subscriptions_sop.dto.SubDto;
import com.example.subscriptions_sop.dto.SubExtendDto;
import com.example.subscriptions_sop.dto.SubUpgradeDto;
import com.example.subscriptions_sop.exceptions.SubscriptionException;
import com.example.subscriptions_sop.exceptions.UserNotFoundException;
import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.model.Subscription;
import com.example.subscriptions_sop.model.SubscriptionTier;
import com.example.subscriptions_sop.model.User;
import com.example.subscriptions_sop.repository.ChannelRepository;
import com.example.subscriptions_sop.repository.SubscriptionRepository;
import com.example.subscriptions_sop.repository.UserRepository;
import com.example.subscriptions_sop.representation_model.SubscriptionRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private final ModelMapper modelMapper;

    public SubscriptionServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CollectionModel<SubscriptionRepresentation> getSubscriptionsHistory(
            String subscriberUsername, String targetChannelUsername, int page, int size) {
        int elementsCount = subscriptionRepository.findBySubscriberUsernameAndTargetChannelOwnerUsername(
                subscriberUsername, targetChannelUsername
        ).size();
        int totalPages = checkPages(page, size, elementsCount);
        Page<Subscription> subscriptions = subscriptionRepository.findBySubscriberUsernameAndTargetChannelOwnerUsername(
                subscriberUsername, targetChannelUsername, PageRequest.of(page - 1, size)
        );
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionsToRepresentations(subscriptions);
        Link selfLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionHistory(
                subscriberUsername, targetChannelUsername, page, size
        )).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUser(subscriberUsername)).withRel("user");
        Link channelLink = linkTo(methodOn(ChannelController.class).getChannel(targetChannelUsername)).withRel("channel");
        collectionModel.add(selfLink).add(userLink).add(channelLink);
        if (page != 1) {
            Link prevLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionHistory(
                    subscriberUsername, targetChannelUsername, page - 1, size
            )).withRel("prev");
            collectionModel.add(prevLink);
        }
        if (page != totalPages) {
            Link nextLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionHistory(
                    subscriberUsername, targetChannelUsername, page + 1, size
            )).withRel("next");
            collectionModel.add(nextLink);
        }
        return collectionModel;
    }

    @Override
    public CollectionModel<SubscriptionRepresentation> getSubscriptionsForUser(String username, int page, int size) {
        int elementsCount = subscriptionRepository.findBySubscriberUsername(username).size();
        int totalPages = checkPages(page, size, elementsCount);
        Page<Subscription> subscriptions = subscriptionRepository.findBySubscriberUsername(
                username, PageRequest.of(page - 1, size)
        );
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionsToRepresentations(subscriptions);
        Link selfLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForUser(
                username, page, size
        )).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUser(username)).withRel("user");
        collectionModel.add(selfLink).add(userLink);
        if (page != 1) {
            Link prevLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForUser(
                    username, page - 1, size
            )).withRel("prev");
            collectionModel.add(prevLink);
        }
        if (page != totalPages) {
            Link nextLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForUser(
                    username, page + 1, size
            )).withRel("next");
            collectionModel.add(nextLink);
        }
        return collectionModel;
    }

    @Override
    public CollectionModel<SubscriptionRepresentation> getSubscriptionsForChannel(String targetChannelUsername,
                                                                                  int page, int size) {
        int elementsCount = subscriptionRepository.findByTargetChannelOwnerUsername(targetChannelUsername).size();
        int totalPages = checkPages(page, size, elementsCount);
        Page<Subscription> subscriptions = subscriptionRepository.findByTargetChannelOwnerUsername(
                targetChannelUsername, PageRequest.of(page - 1, size)
        );
        CollectionModel<SubscriptionRepresentation> collectionModel = subscriptionsToRepresentations(subscriptions);
        Link selfLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForChannel(
                targetChannelUsername, page, size
        )).withSelfRel();
        Link channelLink = linkTo(methodOn(ChannelController.class).getChannel(targetChannelUsername)).withRel("channel");
        collectionModel.add(selfLink).add(channelLink);
        if (page != 1) {
            Link prevLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForChannel(
                    targetChannelUsername, page - 1, size
            )).withRel("prev");
            collectionModel.add(prevLink);
        }
        if (page != totalPages) {
            Link prevLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForChannel(
                    targetChannelUsername, page + 1, size
            )).withRel("next");
            collectionModel.add(prevLink);
        }
        return collectionModel;
    }

    private int checkPages(int page, int size, long elementsCount) {
        if (size <= 0 || size > 50) {
            throw new IllegalArgumentException(
                    String.format("Page size: %d is incorrect, value must be positive number lower than 50", size)
            );
        }
        int totalPages = (int) elementsCount / size + (elementsCount % size == 0 ? 0 : 1);
        if (page > totalPages || page <= 0) {
            throw new IllegalArgumentException(
                    String.format("Page number: %d is incorrect, current available number of pages: 1-%d",
                            page, totalPages)
            );
        }
        return totalPages;
    }

    @Transactional
    @Override
    public SubscriptionRepresentation subscribe(SubDto subDto) {
        String subscriberUsername = subDto.getSubscriberUsername();
        String targetChannelUsername = subDto.getTargetChannelUsername();
        if (subscriberUsername.equals(targetChannelUsername))
            throw new SubscriptionException("You can't subscribe on your own channel");
        Optional<Subscription> activeSubscription = getActiveSubscription(subscriberUsername, targetChannelUsername);
        if (activeSubscription.isPresent())
            throw new SubscriptionException("You already have subscription on this channel " +
                    "try to extend or upgrade your subscription");
        Channel channel = getAndCheckChannel(targetChannelUsername);
        User user = getAndCheckUser(subscriberUsername);
        Double userBalance = user.getBalance();
        SubscriptionTier subscriptionTier = Arrays.stream(SubscriptionTier.values())
                .filter(tier -> tier.getValue() == subDto.getTier()).findFirst().get();
        Double subscriptionPrice = (subscriptionTier.getValue() + 1) * 5D;
        Double subscriptionTotalPrice = subscriptionPrice * subDto.getDurationInMonths();
        if (!channel.isOnline())
            throw new SubscriptionException("Channel that you try to subscribe is offline");
        if (userBalance < subscriptionTotalPrice)
            throw new SubscriptionException("You don't have enough credits");
        Subscription subscription = new Subscription(user, channel, subscriptionTier, subscriptionPrice);
        subscription.setCreated(LocalDateTime.now());
        subscription.setUpdated(LocalDateTime.now());
        subscription.setSubscriptionStartTime(LocalDateTime.now());
        subscription.setSubscriptionEndTime(LocalDateTime.now().plusMonths(subDto.getDurationInMonths()));
        user.setBalance(userBalance - subscriptionTotalPrice);
        SubscriptionRepresentation representation = modelMapper.map(
                subscriptionRepository.saveAndFlush(subscription), SubscriptionRepresentation.class
        );
        userRepository.saveAndFlush(user);
        addLinks(subscription, representation);
        return representation;
    }

    @Transactional
    @Override
    public SubscriptionRepresentation extendSubscription(SubExtendDto subExtendDto) {
        String subscriberUsername = subExtendDto.getSubscriberUsername();
        String targetChannelUsername = subExtendDto.getTargetChannelUsername();
        Optional<Subscription> activeSubscription = getActiveSubscription(subscriberUsername, targetChannelUsername);
        if (activeSubscription.isEmpty())
            throw new SubscriptionException("You don't have any active subscription, try to subscribe");
        Subscription subscription = activeSubscription.get();
        Channel channel = getAndCheckChannel(targetChannelUsername);
        User user = getAndCheckUser(subscriberUsername);
        if (!channel.isOnline())
            throw new SubscriptionException("Channel that you try to subscribe is offline");
        Double userBalance = user.getBalance();
        Double subscriptionPrice = subscription.getPrice();
        if (userBalance < subscriptionPrice)
            throw new SubscriptionException("You don't have enough credits");
        subscription.setSubscriptionEndTime(subscription.getSubscriptionEndTime().plusMonths(subExtendDto.getDurationInMonths()));
        user.setBalance(userBalance - subscriptionPrice);
        SubscriptionRepresentation representation = modelMapper.map(
                subscriptionRepository.saveAndFlush(subscription), SubscriptionRepresentation.class
        );
        userRepository.saveAndFlush(user);
        addLinks(subscription, representation);
        return representation;
    }

    private User getAndCheckUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User " + username + " not found");
        return optionalUser.get();
    }

    private Channel getAndCheckChannel(String username) {
        Optional<Channel> optionalChannel = channelRepository.findByOwnerUsername(username);
        if (optionalChannel.isEmpty())
            throw new UserNotFoundException("User " + username + " not found");
        return optionalChannel.get();
    }

    private Optional<Subscription> getActiveSubscription(String subscriberUsername, String targetChannelUsername) {
        List<Subscription> subscriptions = subscriptionRepository
                .findBySubscriberUsernameAndTargetChannelOwnerUsername(subscriberUsername, targetChannelUsername);
        return subscriptions.stream().filter(Subscription::isActive).findFirst();
    }

    @Override
    public SubscriptionRepresentation cancelSubscription(String subscriberUsername, String targetChannelUsername) {
        Optional<Subscription> activeSubscription = getActiveSubscription(subscriberUsername, targetChannelUsername);
        if (activeSubscription.isEmpty())
            throw new SubscriptionException("You don't have any active subscription, try to subscribe");
        Subscription subscription = activeSubscription.get();
        subscription.setSubscriptionEndTime(LocalDateTime.now());
        subscription.setActive(false);
        SubscriptionRepresentation representation = modelMapper.map(
                subscriptionRepository.saveAndFlush(subscription), SubscriptionRepresentation.class
        );
        addLinks(subscriberUsername, targetChannelUsername, representation);
        return representation;
    }

    @Transactional
    @Override
    public SubscriptionRepresentation upgradeTier(SubUpgradeDto subUpgradeDto) {
        String subscriberUsername = subUpgradeDto.getSubscriberUsername();
        String targetChannelUsername = subUpgradeDto.getTargetChannelUsername();
        Optional<Subscription> activeSubscription = getActiveSubscription(subscriberUsername, targetChannelUsername);
        if (activeSubscription.isEmpty())
            throw new SubscriptionException("You don't have any active subscription, try to subscribe");
        Subscription subscription = activeSubscription.get();
        User user = getAndCheckUser(subscriberUsername);
        Channel channel = getAndCheckChannel(targetChannelUsername);
        if (!channel.isOnline())
            throw new SubscriptionException("Channel that you try to subscribe is offline");
        SubscriptionTier newTier = Arrays.stream(SubscriptionTier.values())
                .filter(tier -> tier.getValue() == subUpgradeDto.getTier()).findFirst().get();
        SubscriptionTier oldTier = subscription.getTier();
        if (newTier.getValue() < oldTier.getValue())
            throw new SubscriptionException("Tier to upgrade must be higher than current tier");
        Double oldSubscriptionPrice = subscription.getPrice();
        Double newSubscriptionPrice = oldSubscriptionPrice + (newTier.getValue() - oldTier.getValue()) * 5;
        Double userBalance = user.getBalance();
        if (userBalance < newSubscriptionPrice)
            throw new SubscriptionException("You don't have enough credits");
        subscription.setPrice(newSubscriptionPrice);
        subscription.setTier(newTier);
        user.setBalance(userBalance - ((newTier.getValue() - oldTier.getValue()) * 5));
        userRepository.saveAndFlush(user);
        SubscriptionRepresentation representation = modelMapper.map(
                subscriptionRepository.saveAndFlush(subscription), SubscriptionRepresentation.class
        );
        addLinks(subscription, representation);
        return representation;
    }

    private CollectionModel<SubscriptionRepresentation> subscriptionsToRepresentations(Page<Subscription> subscriptions) {
        List<SubscriptionRepresentation> representations = subscriptions.stream().map(subscription -> {
            SubscriptionRepresentation representation = modelMapper.map(subscription, SubscriptionRepresentation.class);
            addLinks(subscription, representation);
            return representation;
        }).toList();
        return CollectionModel.of(representations);
    }

    private void addLinks(Subscription subscription, SubscriptionRepresentation representation) {
        addLinks(subscription.getSubscriber().getUsername(),
                subscription.getTargetChannel().getOwner().getUsername(),
                representation
        );
    }

    private void addLinks(String subscriberUsername, String targetChannelUsername,
                          SubscriptionRepresentation representation) {
        Link extendLink = WebMvcLinkBuilder.linkTo(methodOn(SubscriptionController.class)
                        .extendSubscription(null))
                .withRel("extend")
                .withType("PATCH")
                .withTitle("Extend Subscription");
        Link cancelLink = WebMvcLinkBuilder.linkTo(methodOn(SubscriptionController.class)
                        .cancelSubscription(subscriberUsername, targetChannelUsername))
                .withRel("cancel")
                .withType("DELETE")
                .withTitle("Cancel Subscription");
        Link upgradeLink = WebMvcLinkBuilder.linkTo(methodOn(SubscriptionController.class)
                        .upgradeTier(null))
                .withRel("upgrade")
                .withType("PATCH")
                .withTitle("Upgrade Subscription Tier");
        Link selfLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionHistory(
                subscriberUsername, targetChannelUsername, 1, 5
        )).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUser(subscriberUsername)).withRel("user");
        Link channelLink = linkTo(methodOn(ChannelController.class).getChannel(targetChannelUsername)).withRel("channel");
        representation.add(selfLink)
                .add(userLink)
                .add(channelLink)
                .add(extendLink)
                .add(upgradeLink)
                .add(cancelLink);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setChannelRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Autowired
    public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;


    }
}
