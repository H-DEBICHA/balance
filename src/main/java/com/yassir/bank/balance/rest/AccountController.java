package com.yassir.bank.balance.rest;

import com.yassir.bank.balance.dto.AccountResponse;
import com.yassir.bank.balance.dto.TransactionResponse;
import com.yassir.bank.balance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    /**
     *
     * @param customerId the customer Id
     * @param balance the new balance, this field is required
     * @return Created status with the saved account
     */
    @PostMapping("/{customerId}")
    public ResponseEntity<AccountResponse> createAccountForCustomerWithBalance(@PathVariable long customerId,
                                                                               @RequestParam BigDecimal balance) throws Exception {
        log.debug("Rest request to save a new account for the customer ID: {}, with the amount: {}", customerId, balance);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.save(customerId, balance)
        );
    }

    /**
     *
     * @param accountFrom The account source
     * @param accountTo The account target
     * @param amount of the Transfer
     * @return created status with the recipient's account
     */
    @PostMapping("transfer/{accountFrom}/{accountTo}")
    public ResponseEntity<AccountResponse> transferAmount(@PathVariable Long accountFrom,
                                                          @PathVariable Long accountTo,
                                                          @RequestParam BigDecimal amount) {
        log.debug("REST request to transfer an amount: {} from the account : {} to the account: {}", amount, accountFrom, accountTo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.transfer(accountFrom, accountTo, amount)
        );
    }

    /**
     *
     * @param accountId
     * @return status 200 with new balance
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<BigDecimal> getBalanceOfAnAccount(@PathVariable Long accountId) throws Exception {
        log.debug("REST request to get the balance of an account ID: {}", accountId);
        return ResponseEntity.ok().body(
                accountService.getBalance(accountId)
        );
    }

    /**
     *
     * @param accountId
     * @return status 200 with the list of historic transactions
     */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getAccountHistory(@PathVariable Long accountId) throws Exception {
        log.debug("REST request to get history of an account ID: {}", accountId);
        return ResponseEntity.ok().body(
                accountService.getHistoricTransactions(accountId)
        );
    }

    /**
     *
     * @param customerId
     * @return list of account of a customer
     * @throws Exception
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getCustomerAccounts(@PathVariable Long customerId) throws Exception {
        log.debug("REST request to get Account list of a customer: {}", customerId);
        return ResponseEntity.ok().body(
                accountService.getCustomerAccounts(customerId)
        );
    }
}
