package com.yassir.bank.balance.rest;

import com.yassir.bank.balance.dto.AccountResponse;
import com.yassir.bank.balance.dto.TransactionResponse;
import com.yassir.bank.balance.service.AccountService;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private AccountResponse accountResponse;
    private long customerId;
    private BigDecimal balance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = 1L;
        balance = BigDecimal.valueOf(1000);
        List<TransactionResponse> transactionHistories = Collections.emptyList();


        AccountResponse accountResponse = new AccountResponse(199L, customerId, balance, transactionHistories);

    }

    @Test
    public void testCreateAccountForCustomerWithBalance() throws Exception {

        when(accountService.save(customerId, balance)).thenReturn(accountResponse);

        mockMvc.perform(post("/v1/api/accounts/{customerId}", customerId)
                        .param("balance", balance.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testTransferAmount() throws Exception {
        Long accountFrom = 1L;
        Long accountTo = 2L;
        BigDecimal amount = BigDecimal.valueOf(500);

        when(accountService.transfer(accountFrom, accountTo, amount)).thenReturn(accountResponse);

        mockMvc.perform(post("/v1/api/accounts/transfer/{accountFrom}/{accountTo}", accountFrom, accountTo)
                        .param("amount", amount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetBalanceOfAnAccount() throws Exception {
        Long accountId = 1L;
        BigDecimal balance = BigDecimal.valueOf(1000);

        when(accountService.getBalance(accountId)).thenReturn(balance);

        mockMvc.perform(get("/v1/api/accounts/"+ accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(balance));
    }

    @Test
    public void testGetAccountHistory() throws Exception {
        Long accountId = 1L;
        List<TransactionResponse> transactionHistory = Collections.emptyList();

        when(accountService.getHistoricTransactions(accountId)).thenReturn(transactionHistory);

        mockMvc.perform(get("/v1/api/accounts/history/{accountId}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    @Test
    void testGetCustomerAccounts() throws Exception {
        // Arrange
        Long customerId = 1L;

        // Create example AccountResponse objects
        AccountResponse accountResponse1 = new AccountResponse(199L, 1L, BigDecimal.valueOf(1000), null);
        AccountResponse accountResponse2 = new AccountResponse(1999L, 1L, BigDecimal.valueOf(2000), null);

        List<AccountResponse> accountResponses = List.of(accountResponse1, accountResponse2);

        // Mock the service method
        when(accountService.getCustomerAccounts(customerId)).thenReturn(accountResponses);

        // Act & Assert
        mockMvc.perform(get("/v1/api/accounts/customer/"+ customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Expect JSON response
                .andExpect(jsonPath("$", hasSize(2)));

        // Verify that the service method was called
        verify(accountService).getCustomerAccounts(customerId);
    }
}