package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.ChannelUpdateDto;
import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.model.User;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;


public interface ChannelService {
    ChannelRepresentation getChannel(String targetChannelUsername);
    public Channel getChannelData(String targetChannelUsername);
    CollectionModel<ChannelRepresentation> getLiveChannels();
    ChannelRepresentation updateInfo(ChannelUpdateDto channelUpdateDto);
    CollectionModel<UserRepresentation> getSubscribers(String targetChannelUsername);
    ChannelRepresentation goLive(String targetChannelUsername);
    ChannelRepresentation goOffline(String targetChannelUsername);
    public Channel goLiveWithChannelOutput(String targetChannelUsername);
    public Channel goOfflineWithChannelOutput(String targetChannelUsername);
    public List<User> getSubscribersWithUsersOutput(String targetChannelUsername);
}
