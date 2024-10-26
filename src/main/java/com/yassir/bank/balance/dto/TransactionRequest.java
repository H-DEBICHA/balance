package com.yassir.bank.balance.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(
        @NotNull(message = "The 'fromAccount' shouldn't be null")
        AccountRequest fromAccount,

        @NotNull(message = "The 'toAccount' shouldn't be null")
        AccountRequest toAccount,

        @NotNull(message = "The 'amount' shouldn't be null")
        BigDecimal amount,
        
        @NotNull(message = "The 'fromAccountOldBalance' shouldn't be null")
        BigDecimal fromAccountOldBalance,
        
        @NotNull(message = "The 'fromAccountNewBalance' shouldn't be null")
        BigDecimal fromAccountNewBalance,

        @NotNull(message = "The 'toAccountOldBalance' shouldn't be null")
        BigDecimal toAccountOldBalance,
        
        @NotNull(message = "The 'toAccountNewBalance' shouldn't be null")
        BigDecimal toAccountNewBalance,
        
        @NotNull(message = "The 'timestamp' shouldn't be null")
        LocalDateTime timestamp) {}
