package com.example.subscriptions_sop.fetcher.query;

import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.service.ChannelService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class ChannelQuery {
    private ChannelService channelService;

    @DgsQuery
    public Channel channel(@InputArgument(name = "username") String username) {
        return channelService.getChannelData(username);
    }

    @Autowired
    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }
}
