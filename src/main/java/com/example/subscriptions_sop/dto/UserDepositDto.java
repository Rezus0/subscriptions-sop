package com.example.subscriptions_sop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class UserDepositDto {
    private String username;
    private Double depositAmount;

    @NotBlank(message = "Username can't be blank")
    public String getUsername() {
        return username;
    }

    @Positive(message = "Deposit amount must be bigger than 0")
    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }
}
