package com.example.subscriptions_sop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Entity
@Table(name = "channels")
public class Channel extends BaseEntity {
    private User owner;
    private Set<Subscription> subscriptions;
    private boolean isOnline = false;
    private String description;
    private Set<String> mediaLinks;

    @OneToOne
    @JoinColumn(name = "owner_id")
    @NotNull(message = "Owner can't be null")
    public User getOwner() {
        return owner;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "targetChannel")
    @BatchSize(size = 10)
    @NotNull(message = "Subscriptions can't be null")
    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @NotNull(message = "Online status can't be null")
    public boolean isOnline() {
        return isOnline;
    }

    @NotBlank(message = "Description can't be blank")
    public String getDescription() {
        return description;
    }

    @ElementCollection
    @CollectionTable(name = "channel_links", joinColumns = @JoinColumn(name = "channel_id"))
    @NotNull(message = "Links can't be null")
    public Set<String> getMediaLinks() {
        return mediaLinks;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMediaLinks(Set<String> links) {
        this.mediaLinks = links;
    }
}
