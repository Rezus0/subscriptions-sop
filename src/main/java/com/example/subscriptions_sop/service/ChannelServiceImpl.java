package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.controller.ChannelController;
import com.example.subscriptions_sop.controller.SubscriptionController;
import com.example.subscriptions_sop.controller.UserController;
import com.example.subscriptions_sop.dto.ChannelUpdateDto;
import com.example.subscriptions_sop.exceptions.UserAlreadyOnlineException;
import com.example.subscriptions_sop.exceptions.UserNotFoundException;
import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.model.User;
import com.example.subscriptions_sop.repository.ChannelRepository;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ChannelServiceImpl implements ChannelService {

    private ChannelRepository channelRepository;
    private final ModelMapper modelMapper;

    public ChannelServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ChannelRepresentation getChannel(String targetChannelUsername) {
        Channel channel = getChannelData(targetChannelUsername);
        ChannelRepresentation channelRepresentation = modelMapper.map(channel, ChannelRepresentation.class);
        addLinks(channelRepresentation, targetChannelUsername);
        return channelRepresentation;
    }

    @Override
    public Channel getChannelData(String targetChannelUsername) {
        Optional<Channel> optionalChannel = channelRepository.findByOwnerUsername(targetChannelUsername);
        if (optionalChannel.isEmpty())
            throw new UserNotFoundException("User not found");
        return optionalChannel.get();
    }

    @Override
    public CollectionModel<ChannelRepresentation> getLiveChannels() {
        return null;
    }

    @Override
    public ChannelRepresentation updateInfo(ChannelUpdateDto channelUpdateDto) {
        return null;
    }

    @Override
    public CollectionModel<UserRepresentation> getSubscribers(String targetChannelUsername) { //todo доделать
        return null;
    }

    @Override
    public List<User> getSubscribersWithUsersOutput(String targetChannelUsername) {
        Optional<Channel> optionalChannel = channelRepository.findByOwnerUsername(targetChannelUsername);
        if (optionalChannel.isEmpty())
            throw new UserNotFoundException("User not found");
        Channel channel = optionalChannel.get();
        List<User> subscribers = new ArrayList<>();
        channel.getSubscriptions().forEach(s -> {
            if (s.isActive()) {
                subscribers.add(s.getSubscriber());
            }
        });
        return subscribers;
    }


    @Override
    public ChannelRepresentation goLive(String targetChannelUsername) {
        Channel channel = goLiveWithChannelOutput(targetChannelUsername);
        ChannelRepresentation channelRepresentation = modelMapper.map(channel, ChannelRepresentation.class);
        addLinks(channelRepresentation, targetChannelUsername);
        return channelRepresentation;
    }

    @Override
    public Channel goLiveWithChannelOutput(String targetChannelUsername) {
        Optional<Channel> optionalChannel = channelRepository.findByOwnerUsername(targetChannelUsername);
        if (optionalChannel.isEmpty())
            throw new UserNotFoundException("User not found");
        Channel channel = optionalChannel.get();
        if (!channel.isOnline())
            channel.setOnline(true);
        else
            throw new UserAlreadyOnlineException("Channel already online");
        return channelRepository.saveAndFlush(channel);
    }

    @Override
    public ChannelRepresentation goOffline(String targetChannelUsername) {
        Channel channel = goOfflineWithChannelOutput(targetChannelUsername);
        ChannelRepresentation channelRepresentation = modelMapper.map(channel, ChannelRepresentation.class);
        addLinks(channelRepresentation, targetChannelUsername);
        return channelRepresentation;
    }

    @Override
    public Channel goOfflineWithChannelOutput(String targetChannelUsername) {
        Optional<Channel> optionalChannel = channelRepository.findByOwnerUsername(targetChannelUsername);
        if (optionalChannel.isEmpty())
            throw new UserNotFoundException("User not found");
        Channel channel = optionalChannel.get();
        if (channel.isOnline())
            channel.setOnline(false);
        return channelRepository.saveAndFlush(channel);
    }

    private void addLinks(ChannelRepresentation representation, String targetChannelUsername) {
        Link selfLink = linkTo(methodOn(ChannelController.class).getChannel(targetChannelUsername)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUser(targetChannelUsername)).withRel("user");
        Link subsLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForChannel(
                targetChannelUsername, 1, 5
        )).withRel("subscriptions");
        Link goLiveLink = linkTo(methodOn(ChannelController.class).goLive(targetChannelUsername))
                .withRel("goLive")
                .withType("GET")
                .withName("Go Live");
        representation.add(selfLink).add(userLink).add(subsLink).add(goLiveLink);
    }

    @Autowired
    public void setChannelRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
}
