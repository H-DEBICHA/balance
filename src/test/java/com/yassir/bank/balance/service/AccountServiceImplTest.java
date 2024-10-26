package com.yassir.bank.balance.service;

import com.yassir.bank.balance.dto.AccountRequest;
import com.yassir.bank.balance.dto.AccountResponse;
import com.yassir.bank.balance.dto.TransactionResponse;
import com.yassir.bank.balance.mapper.AccountMapper;
import com.yassir.bank.balance.mapper.TransactionMapper;
import com.yassir.bank.balance.model.Account;
import com.yassir.bank.balance.model.Customer;
import com.yassir.bank.balance.model.Transaction;
import com.yassir.bank.balance.repository.AccountRepository;
import com.yassir.bank.balance.repository.CustomerRepository;
import com.yassir.bank.balance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.ExpressionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John Doe");
        account = new Account(1L, new BigDecimal("1000.00"), customer);
    }

    @Test
    void save_ShouldCreateNewAccount_WhenCustomerExists() throws Exception {
        BigDecimal initialDeposit = new BigDecimal("500.00");

        when(customerRepository.existsById(customer.getId())).thenReturn(true);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(new AccountResponse(1991L, customer.getId(), account.getBalance(), null));

        AccountResponse result = accountService.save(customer.getId(), initialDeposit);

        assertNotNull(result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void save_ShouldThrowException_WhenCustomerDoesNotExist() {
        BigDecimal initialDeposit = new BigDecimal("500.00");

        when(customerRepository.existsById(customer.getId())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> accountService.save(customer.getId(), initialDeposit));

        assertEquals("There is no Customer with this ID: " + customer.getId(), exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void transfer_ShouldTransferAmount_WhenAccountsExistAndBalanceIsSufficient() {
        // Arrange
        Long accountFromId = 1L;
        Long accountToId = 2L;
        BigDecimal transferAmount = new BigDecimal("200.00");
        Account fromAccount = new Account(accountFromId, new BigDecimal("1000.00"), customer);
        Account toAccount = new Account(accountToId, new BigDecimal("500.00"), customer);

        when(accountRepository.findById(accountFromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(accountToId)).thenReturn(Optional.of(toAccount));
        when(accountMapper.toDto(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            return new AccountResponse(2024L, customer.getId(), account.getBalance(), null);
        });

        // Capture the transaction being saved
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        // Act
        AccountResponse result = accountService.transfer(accountFromId, accountToId, transferAmount);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("800.00"), fromAccount.getBalance()); // Updated fromAccount balance
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());   // Updated toAccount balance

        // Verify that the transaction was saved and capture the transaction details
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        // Check the transaction details
        assertEquals(fromAccount, savedTransaction.getFromAccount());
        assertEquals(toAccount, savedTransaction.getToAccount());
        assertEquals(transferAmount, savedTransaction.getAmount());
        assertEquals(new BigDecimal("1000.00"), savedTransaction.getFromAccountOldBalance());
        assertEquals(new BigDecimal("800.00"), savedTransaction.getFromAccountNewBalance());
        assertEquals(new BigDecimal("500.00"), savedTransaction.getToAccountOldBalance());
        assertEquals(new BigDecimal("700.00"), savedTransaction.getToAccountNewBalance());
        assertNotNull(savedTransaction.getTimestamp()); // Ensure timestamp is set
    }

    @Test
    void transfer_ShouldThrowException_WhenFromAccountDoesNotExist() {
        Long nonExistentAccountId = 99L;

        when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        ExpressionException exception = assertThrows(ExpressionException.class, () ->
                accountService.transfer(nonExistentAccountId, account.getId(), new BigDecimal("100.00"))
        );

        assertEquals("There is no Account with this ID: " + nonExistentAccountId, exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void getHistoricTransactions_ShouldReturnTransactionHistory_WhenAccountExists() throws Exception {
        // Arrange
        Long accountId = 1L;
        Account account = new Account(accountId, new BigDecimal("800.00"), customer);

        // Create a dummy transaction
        Account fromAccount = new Account(accountId, new BigDecimal("800.00"), customer);
        Account toAccount = new Account(2L, new BigDecimal("500.00"), customer);

        Transaction transaction1 = new Transaction();
        transaction1.setFromAccount(fromAccount);
        transaction1.setToAccount(toAccount);
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setFromAccountOldBalance(fromAccount.getBalance());
        transaction1.setFromAccountNewBalance(new BigDecimal("700.00")); // After transfer
        transaction1.setToAccountOldBalance(toAccount.getBalance());
        transaction1.setToAccountNewBalance(new BigDecimal("600.00")); // After transfer
        transaction1.setTimestamp(LocalDateTime.now());

        // Create expected TransactionResponse
        AccountResponse fromAccountResponse = new AccountResponse(null, fromAccount.getCustomer().getId(), fromAccount.getBalance(), null);
        AccountResponse toAccountResponse = new AccountResponse(null, toAccount.getCustomer().getId(), toAccount.getBalance(), null);

        TransactionResponse expectedResponse = new TransactionResponse(
                fromAccountResponse,
                toAccountResponse,
                transaction1.getAmount(),
                transaction1.getFromAccountOldBalance(),
                transaction1.getFromAccountNewBalance(),
                transaction1.getToAccountOldBalance(),
                transaction1.getToAccountNewBalance(),
                transaction1.getTimestamp()
        );

        // Mock repository behavior
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findByFromAccountOrToAccount(account, account)).thenReturn(List.of(transaction1)); // Use the same instance
        when(transactionMapper.toDto(transaction1)).thenReturn(expectedResponse);

        // Act
        List<TransactionResponse> result = accountService.getHistoricTransactions(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedResponse, result.get(0));
    }
    @Test
    void getHistoricTransactions_ShouldThrowException_WhenAccountDoesNotExist() {
        // Arrange
        Long accountId = 1L;

        when(accountRepository.existsById(accountId)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> accountService.getHistoricTransactions(accountId));
        assertEquals("There is no account with this ID:" + accountId, exception.getMessage());
    }

    @Test
    void testGetCustomerAccounts() {
        // Arrange
        Long customerId = 1L;

        // Mocking the repository to return a list of Account objects
        Account account1 = new Account();
        account1.setCustomer(Customer.builder().id(customerId).build());

        Account account2 = new Account();
        account2.setCustomer(Customer.builder().id(customerId).build());

        List<Account> accounts = List.of(account1, account2);

        when(accountRepository.findByCustomerId(customerId)).thenReturn(accounts);
        customerId = 1L;
        List<TransactionResponse> transactionHistories = Collections.emptyList();

        // Mocking the mapper to return AccountResponse objects
        AccountResponse accountResponse1 = new AccountResponse(199L, 1L, BigDecimal.valueOf(1000), transactionHistories);
        AccountResponse accountResponse2 = new AccountResponse(1999L, 1L, BigDecimal.valueOf(2000), transactionHistories);

        when(accountMapper.toDto(account1)).thenReturn(accountResponse1);
        when(accountMapper.toDto(account2)).thenReturn(accountResponse2);

        // Act
        List<AccountResponse> result = accountService.getCustomerAccounts(customerId);

        // Assert
        assertEquals(2, result.size()); // Check the size of the response
        assertEquals(accountResponse1, result.get(0)); // Check the first mapped response
        assertEquals(accountResponse2, result.get(1)); // Check the second mapped response

        // Verify interactions
        verify(accountRepository).findByCustomerId(customerId);
        verify(accountMapper).toDto(account1);
        verify(accountMapper).toDto(account2);
    }
}