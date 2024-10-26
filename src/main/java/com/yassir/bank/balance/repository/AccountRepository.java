package com.yassir.bank.balance.repository;

import com.yassir.bank.balance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
