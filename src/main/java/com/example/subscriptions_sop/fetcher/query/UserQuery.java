package com.example.subscriptions_sop.fetcher.query;

import com.example.subscriptions_sop.model.User;
import com.example.subscriptions_sop.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UserQuery {
    private UserService userService;

    @DgsQuery
    public User user(@InputArgument(name = "username") String username) {
        return userService.getUserData(username);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
