package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.controller.ChannelController;
import com.example.subscriptions_sop.controller.SubscriptionController;
import com.example.subscriptions_sop.controller.UserController;
import com.example.subscriptions_sop.dto.ChannelUpdateDto;
import com.example.subscriptions_sop.exceptions.UserNotFoundException;
import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.repository.ChannelRepository;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

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
        Optional<Channel> optionalChannel = channelRepository.findByOwnerUsername(targetChannelUsername);
        if (optionalChannel.isEmpty())
            throw new UserNotFoundException("User not found");
        Channel channel = optionalChannel.get();
        ChannelRepresentation channelRepresentation = modelMapper.map(channel, ChannelRepresentation.class);
        addLinks(channelRepresentation, targetChannelUsername);
        return channelRepresentation;
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
    public CollectionModel<UserRepresentation> getSubscribers(String targetChannelUsername) {
        return null;
    }

    @Override
    public ChannelRepresentation goLive(String targetChannelUsername) {
        Optional<Channel> optionalChannel = channelRepository.findByOwnerUsername(targetChannelUsername);
        if (optionalChannel.isEmpty())
            throw new UserNotFoundException("User not found");
        Channel channel = optionalChannel.get();
        if (!channel.isOnline())
            channel.setOnline(true);
        channelRepository.saveAndFlush(channel);
        ChannelRepresentation channelRepresentation = modelMapper.map(channel, ChannelRepresentation.class);
        addLinks(channelRepresentation, targetChannelUsername);
        return channelRepresentation;
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
