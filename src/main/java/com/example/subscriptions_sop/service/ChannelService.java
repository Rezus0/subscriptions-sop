package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.ChannelUpdateDto;
import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;


public interface ChannelService {
    ChannelRepresentation getChannel(String targetChannelUsername);
    public Channel getChannelData(String targetChannelUsername);
    CollectionModel<ChannelRepresentation> getLiveChannels();
    ChannelRepresentation updateInfo(ChannelUpdateDto channelUpdateDto);
    CollectionModel<UserRepresentation> getSubscribers(String targetChannelUsername);
    ChannelRepresentation goLive(String targetChannelUsername);
    public Channel getChannelDataForLive(String targetChannelUsername);
}
