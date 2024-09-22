package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.dto.UserDepositDto;
import com.example.subscriptions_sop.dto.UserRegDto;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

public interface UserService {
    UserRepresentation getUser(String username);
    UserRepresentation register(UserRegDto userRegDto);
    UserRepresentation update(UserRegDto userRegDto);
    UserRepresentation deposit(UserDepositDto userDepositDto);
    CollectionModel<ChannelRepresentation> getSubscribedChannels(String subscriberUsername);
}
