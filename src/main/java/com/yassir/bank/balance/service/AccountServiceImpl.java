package com.yassir.bank.balance.service;

import com.yassir.bank.balance.dto.AccountResponse;
import com.yassir.bank.balance.dto.TransactionResponse;
import com.yassir.bank.balance.exception.EntityAlreadyExist;
import com.yassir.bank.balance.exception.ExceptionMessage;
import com.yassir.bank.balance.mapper.AccountMapper;
import com.yassir.bank.balance.mapper.TransactionMapper;
import com.yassir.bank.balance.model.Account;
import com.yassir.bank.balance.model.Customer;
import com.yassir.bank.balance.model.Transaction;
import com.yassir.bank.balance.repository.AccountRepository;
import com.yassir.bank.balance.repository.CustomerRepository;
import com.yassir.bank.balance.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public AccountResponse save(Long customerId, BigDecimal amount) throws Exception {
        if (!customerRepository.existsById(customerId))
            throw new EntityAlreadyExist(ExceptionMessage.NEW_ENTITY_SHOULD_NOT_HAVE_AN_ID.getMessage() + " " + customerId);

        Customer customer = customerRepository.findById(customerId).get();
        Account account = new Account(customer, amount);

        return accountMapper.toDto(
                accountRepository.save(account)
        );
    }

    @Override
    public AccountResponse transfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Account from = accountRepository.findById(accountFrom)
                .orElseThrow(() -> new EntityAlreadyExist(ExceptionMessage.NEW_ENTITY_SHOULD_NOT_HAVE_AN_ID.getMessage() + " " +accountFrom));


        Account to = accountRepository.findById(accountTo)
                .orElseThrow(() -> new EntityAlreadyExist(ExceptionMessage.NEW_ENTITY_SHOULD_NOT_HAVE_AN_ID.getMessage() + " " +accountTo));

        BigDecimal fromOldBalance = from.getBalance();
        BigDecimal toOldBalance = to.getBalance();

        // Reducing the amount from the donor's account
        BigDecimal fromNewBalance = fromOldBalance.subtract(amount);
        // Adding the amount to the recipient's account
        BigDecimal toNewBalance = toOldBalance.add(amount);

        from.setBalance(fromNewBalance);
        to.setBalance(toNewBalance);

        Transaction transaction = Transaction.builder()
                .fromAccount(from)
                .toAccount(to)
                .amount(amount)
                .fromAccountOldBalance(fromOldBalance)
                .fromAccountNewBalance(fromNewBalance)
                .toAccountOldBalance(toOldBalance)
                .toAccountNewBalance(toNewBalance)
                .timestamp(LocalDateTime.now())
                .build();

        accountRepository.save(from);
        accountRepository.save(to);
        transactionRepository.save(transaction);

        return accountMapper.toDto(to);
    }

    @Override
    public BigDecimal getBalance(Long accountId) throws Exception {
        if (!accountRepository.existsById(accountId))
            throw new EntityAlreadyExist(ExceptionMessage.NEW_ENTITY_SHOULD_NOT_HAVE_AN_ID.getMessage() + " " + accountId);

        Account account = accountRepository.findById(accountId).get();

        return account.getBalance();
    }

    @Override
    public List<TransactionResponse> getHistoricTransactions(Long accountId) throws Exception {
        if (!accountRepository.existsById(accountId))
            throw new EntityAlreadyExist(ExceptionMessage.NEW_ENTITY_SHOULD_NOT_HAVE_AN_ID.getMessage() + " " + accountId);

        Account account = accountRepository.findById(accountId).get();

        return transactionRepository.findByFromAccountOrToAccount(account, account)
                .stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> getCustomerAccounts(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream().map(accountMapper::toDto).toList();
    }
}
