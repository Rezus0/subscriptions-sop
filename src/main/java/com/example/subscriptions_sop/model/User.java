package com.example.subscriptions_sop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String username;
    private String password;
    private Channel channel;
    private Double balance = 0D;
    private Set<Subscription> subscriptions = new HashSet<>();

    public void addToBalance(Double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit amount must be positive number");
        this.balance += amount;
    }

    @NotBlank(message = "Username can't be blank")
    public String getUsername() {
        return username;
    }

    @NotBlank(message = "Password can't be blank")
    public String getPassword() {
        return password;
    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "channel_id")
    public Channel getChannel() {
        return channel;
    }

    @PositiveOrZero(message = "Balance can't be lower than 0")
    public Double getBalance() {
        return balance;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "subscriber")
    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
