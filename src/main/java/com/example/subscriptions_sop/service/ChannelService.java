package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.ChannelUpdateDto;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;


public interface ChannelService {
    EntityModel<ChannelRepresentation> getChannel(String targetChannelUsername);
    CollectionModel<ChannelRepresentation> getLiveChannels();
    EntityModel<ChannelRepresentation> updateInfo(ChannelUpdateDto channelUpdateDto);
    CollectionModel<UserRepresentation> getSubscribers(String targetChannelUsername);
    EntityModel<ChannelRepresentation> goLive(String targetChannelUsername);
}
