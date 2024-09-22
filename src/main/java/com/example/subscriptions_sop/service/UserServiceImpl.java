package com.example.subscriptions_sop.service;

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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private final ModelMapper modelMapper;

    @Value("${app.url.base}")
    private String urlBase;

    public UserServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserRepresentation getUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        User user = optionalUser.get();
        UserRepresentation userRepresentation = modelMapper.map(
                user,
                UserRepresentation.class
        );
        addLinks(username, userRepresentation);
        return userRepresentation;
    }

    @Override
    public UserRepresentation register(UserRegDto userRegDto) {
        if (userRepository.findByUsername(userRegDto.getUsername()).isPresent())
            throw new UserAlreadyExistException("User already exist");
        User newUser = modelMapper.map(userRegDto, User.class);
        Channel newChannel = new Channel(newUser, "");
        newUser.setCreated(LocalDateTime.now());
        newUser.setUpdated(LocalDateTime.now());
        newUser.setChannel(newChannel);
        UserRepresentation userRepresentation = modelMapper.map(
                userRepository.saveAndFlush(newUser),
                UserRepresentation.class
        );
        addLinks(newUser.getUsername(), userRepresentation);
        return userRepresentation;
    }

    @Override
    public UserRepresentation update(UserRegDto userRegDto) {
        return null;
    }

    @Override
    public UserRepresentation deposit(UserDepositDto userDepositDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDepositDto.getUsername());
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        User user = optionalUser.get();
        user.addToBalance(userDepositDto.getDepositAmount());
        UserRepresentation userRepresentation = modelMapper.map(
                userRepository.saveAndFlush(user), UserRepresentation.class
        );
        return addLinks(user.getUsername(), userRepresentation);
    }

    @Override
    public CollectionModel<ChannelRepresentation> getSubscribedChannels(String subscriberUsername) {
        return null;
    }

    private UserRepresentation addLinks(String username, UserRepresentation userRepresentation) {
        userRepresentation.add(Link.of(urlBase + username).withSelfRel())
                .add(Link.of(urlBase + username + "/channel").withRel("channel"))
                .add(Link.of(urlBase + username + "/subscriptions").withRel("subscriptions"));
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
