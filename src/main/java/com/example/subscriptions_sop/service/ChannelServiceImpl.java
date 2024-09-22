package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.ChannelUpdateDto;
import com.example.subscriptions_sop.exceptions.UserNotFoundException;
import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.repository.ChannelRepository;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChannelServiceImpl implements ChannelService {

    private ChannelRepository channelRepository;
    private final ModelMapper modelMapper;

    @Value("${app.url.base}")
    private String urlBase;

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
        channelRepresentation.add(Link.of(urlBase + targetChannelUsername + "/channel").withSelfRel())
                .add(Link.of(urlBase + targetChannelUsername).withRel("user"))
                .add(Link.of(urlBase + targetChannelUsername + "/channel" + "/subscriptions")
                        .withRel("subscriptions")
                );
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
        return null;
    }

    @Autowired
    public void setChannelRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
}
