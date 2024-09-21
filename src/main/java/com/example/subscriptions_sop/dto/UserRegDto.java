package com.example.subscriptions_sop.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRegDto {
    private String username;
    private String password;

    @NotBlank(message = "Username can't be blank")
    public String getUsername() {
        return username;
    }

    @NotBlank(message = "Password can't be blank")
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
