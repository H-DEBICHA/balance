package com.yassir.bank.balance.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank(message = "The name shouldn't be blank")
        String name) {}
