package com.yassir.bank.balance.service;

import com.yassir.bank.balance.dto.AccountResponse;
import com.yassir.bank.balance.dto.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponse save(Long customerId, BigDecimal amount) throws Exception;

    AccountResponse transfer(Long accountFrom, Long accountTo, BigDecimal amount);

    BigDecimal getBalance(Long accountId) throws Exception;

    List<TransactionResponse> getHistoricTransactions(Long accountId) throws Exception;

    List<AccountResponse> getCustomerAccounts(Long customerId);
}
