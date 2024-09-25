package com.example.subscriptions_sop.controller;

import com.example.subscriptions_sop.dto.UserDepositDto;
import com.example.subscriptions_sop.dto.UserRegDto;
import com.example.subscriptions_sop.representation_model.UserRepresentation;
import com.example.subscriptions_sop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/")
public class UserController {

    private UserService userService;
    @Value("${app.url.base}")
    private String urlBase;

    @GetMapping("/{username}")
    public ResponseEntity<UserRepresentation> getUser(@PathVariable String username) {
        UserRepresentation representation = userService.getUser(username);
        return ResponseEntity.ok(representation);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRepresentation> register(@RequestBody UserRegDto userRegDto) {
        UserRepresentation representation = userService.register(userRegDto);
        return ResponseEntity.created(URI.create(urlBase + representation.getUsername())).body(representation);
    }

    @PatchMapping("/user/deposit")
    public ResponseEntity<UserRepresentation> deposit(@RequestBody UserDepositDto userDepositDto) {
        UserRepresentation representation = userService.deposit(userDepositDto);
        return ResponseEntity.ok(representation);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
