package com.example.subscriptions_sop.fetcher.mutation;

import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.service.ChannelService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class ChannelMutation {
    private ChannelService channelService;

    @DgsMutation
    public Channel goLive(@InputArgument("targetChannelUsername") String targetChannelUsername) {
        return channelService.getChannelDataForLive(targetChannelUsername);
    }

    @Autowired
    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }
}
