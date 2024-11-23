package com.example.subscriptions_sop.fetcher.mutation;

import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.service.ChannelService;
import com.example.subscriptions_sop.service.RabbitMQService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class ChannelMutation {

    private RabbitMQService rabbitMQService;

    private ChannelService channelService;

    @DgsMutation
    public Channel goLive(@InputArgument("targetChannelUsername") String targetChannelUsername) {
        Channel channel = channelService.goLiveWithChannelOutput(targetChannelUsername);
        rabbitMQService.sendGoLiveMessageForChannelSubscribers(targetChannelUsername);
        return channel;
    }

    @DgsMutation
    public Channel goOffline(@InputArgument("targetChannelUsername") String targetChannelUsername) {
        return channelService.goOfflineWithChannelOutput(targetChannelUsername);
    }

    @Autowired
    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Autowired
    public void setRabbitMQService(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }
}
