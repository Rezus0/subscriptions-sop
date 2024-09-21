package com.example.subscriptions_sop.representation_model;

import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

public class ChannelRepresentation extends RepresentationModel<ChannelRepresentation> {
    private boolean isOnline;
    private String description;
    private Set<String> mediaLinks;

    public ChannelRepresentation() {
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getMediaLinks() {
        return mediaLinks;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMediaLinks(Set<String> mediaLinks) {
        this.mediaLinks = mediaLinks;
    }
}
