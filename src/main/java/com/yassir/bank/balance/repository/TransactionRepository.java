package com.yassir.bank.balance.repository;

import com.yassir.bank.balance.model.Account;
import com.yassir.bank.balance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountOrToAccount(Account account, Account account1);
}
