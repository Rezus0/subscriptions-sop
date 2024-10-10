package com.example.subscriptions_sop.service;

import com.example.subscriptions_sop.controller.ChannelController;
import com.example.subscriptions_sop.controller.SubscriptionController;
import com.example.subscriptions_sop.controller.UserController;
import com.example.subscriptions_sop.dto.UserDepositDto;
import com.example.subscriptions_sop.dto.UserRegDto;
import com.example.subscriptions_sop.exceptions.UserAlreadyExistException;
import com.example.subscriptions_sop.exceptions.UserNotFoundException;
import com.example.subscriptions_sop.model.Channel;
import com.example.subscriptions_sop.model.User;
import com.example.subscriptions_sop.repository.ChannelRepository;
import com.example.subscriptions_sop.repository.UserRepository;
import com.example.subscriptions_sop.representation_model.ChannelRepresentation;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserRepresentation getUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        User user = getUserData(username);
        UserRepresentation userRepresentation = modelMapper.map(
                user,
                UserRepresentation.class
        );
        addLinks(username, userRepresentation);
        return userRepresentation;
    }

    @Override
    public User getUserData(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        return optionalUser.get();
    }


    @Override
    public UserRepresentation register(UserRegDto userRegDto) {
        User newUser = getUserDataForRegister(userRegDto);
        UserRepresentation userRepresentation = modelMapper.map(
                newUser,
                UserRepresentation.class
        );
        addLinks(newUser.getUsername(), userRepresentation);
        return userRepresentation;
    }

    @Override
    public User getUserDataForRegister(UserRegDto userRegDto) {
        if (userRepository.findByUsername(userRegDto.getUsername()).isPresent())
            throw new UserAlreadyExistException("User already exist");
        User newUser = modelMapper.map(userRegDto, User.class);
        Channel newChannel = new Channel(newUser, "");
        newUser.setCreated(LocalDateTime.now());
        newUser.setUpdated(LocalDateTime.now());
        newUser.setChannel(newChannel);
        return userRepository.saveAndFlush(newUser);
    }

    @Override
    public UserRepresentation update(UserRegDto userRegDto) {
        return null;
    }

    @Override
    public UserRepresentation deposit(UserDepositDto userDepositDto) {
        User user = getUserDataForDeposit(userDepositDto);
        UserRepresentation userRepresentation = modelMapper.map(
                user, UserRepresentation.class
        );
        return addLinks(user.getUsername(), userRepresentation);
    }

    @Override
    public User getUserDataForDeposit(UserDepositDto userDepositDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDepositDto.getUsername());
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        User user = optionalUser.get();
        user.addToBalance(userDepositDto.getDepositAmount());
        return userRepository.saveAndFlush(user);
    }


    @Override
    public CollectionModel<ChannelRepresentation> getSubscribedChannels(String subscriberUsername) {
        return null;
    }

    private UserRepresentation addLinks(String username, UserRepresentation userRepresentation) {
        Link selfLink = linkTo(methodOn(UserController.class).getUser(username)).withSelfRel();
        Link channelLink = linkTo(methodOn(ChannelController.class).getChannel(username)).withRel("channel");
        Link subsLink = linkTo(methodOn(SubscriptionController.class).getSubscriptionsForUser(
                username, 1, 5
        )).withRel("subscriptions");
        Link depositLink = linkTo(methodOn(UserController.class).deposit(null)).withRel("deposit")
                .withName("Deposit")
                .withType("PATCH");
        userRepresentation.add(selfLink).add(channelLink).add(subsLink).add(depositLink);
        return userRepresentation;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setChannelRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
}
