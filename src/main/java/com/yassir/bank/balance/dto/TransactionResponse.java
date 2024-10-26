package com.yassir.bank.balance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        AccountRequest fromAccount,

        AccountRequest toAccount,

        BigDecimal amount,

        BigDecimal fromAccountOldBalance,

        BigDecimal fromAccountNewBalance,

        BigDecimal toAccountOldBalance,
        
        BigDecimal toAccountNewBalance,
        
        LocalDateTime timestamp) {}
