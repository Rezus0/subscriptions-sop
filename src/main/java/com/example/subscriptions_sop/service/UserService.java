package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.UserDepositDto;
import com.example.subscriptions_sop.dto.UserRegDto;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface UserService {
    EntityModel<UserRepresentation> getUser(String username);
    EntityModel<UserRepresentation> register(UserRegDto userRegDto);
    EntityModel<UserRepresentation> update(UserRegDto userRegDto);
    EntityModel<UserRepresentation> deposit(UserDepositDto userDepositDto);
    CollectionModel<ChannelRepresentation> getSubscribedChannels(String subscriberUsername);
}
