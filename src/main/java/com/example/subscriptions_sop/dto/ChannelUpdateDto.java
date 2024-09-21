package com.example.subscriptions_sop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

public class ChannelUpdateDto {
    private String description;
    private Set<String> mediaLinks;

    @NotBlank(message = "Description can't be blank")
    public String getDescription() {
        return description;
    }

    @NotNull(message = "Media links can't be blank")
    public Set<@URL(message = "Each media link must be a valid URL") String> getMediaLinks() {
        return mediaLinks;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMediaLinks(Set<String> mediaLinks) {
        this.mediaLinks = mediaLinks;
    }
}
