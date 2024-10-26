package com.yassir.bank.balance.repository;

import com.yassir.bank.balance.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
