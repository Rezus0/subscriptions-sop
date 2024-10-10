package com.example.subscriptions_sop.fetcher.mutation;

import com.example.subscriptions_sop.dto.UserDepositDto;
import com.example.subscriptions_sop.dto.UserRegDto;
import com.example.subscriptions_sop.model.User;
import com.example.subscriptions_sop.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UserMutation {
    private UserService userService;

    @DgsMutation
    public User registerUser(@InputArgument("user") UserRegDto userRegDto) {
        return userService.getUserDataForRegister(userRegDto);
    }

    @DgsMutation
    public User deposit(@InputArgument("user") UserDepositDto userDepositDto) {
        return userService.getUserDataForDeposit(userDepositDto);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
