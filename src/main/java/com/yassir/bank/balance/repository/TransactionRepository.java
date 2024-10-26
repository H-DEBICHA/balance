package com.yassir.bank.balance.repository;

import com.yassir.bank.balance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
