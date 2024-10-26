package com.yassir.bank.balance.dto;

import java.math.BigDecimal;

public record AccountResponse(
        Long customerId,
        BigDecimal balance
) {
}
