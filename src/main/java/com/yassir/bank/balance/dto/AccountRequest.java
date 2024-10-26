package com.yassir.bank.balance.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountRequest(
        @NotNull(message = "The 'customerId' shouldn't be null")
        Long customerId,
        @NotNull(message = "The 'balance' shouldn't be null")
        BigDecimal balance
) {
}
