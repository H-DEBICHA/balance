package com.yassir.bank.balance.dto;

import java.math.BigDecimal;
import java.util.List;

public record AccountResponse(
        Long id,
        Long customerId,
        BigDecimal balance,
        List<TransactionResponse> transactionHistories) {}
