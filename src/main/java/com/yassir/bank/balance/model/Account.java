package com.yassir.bank.balance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The 'balance' shouldn't be null")
    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    @ManyToOne
    @NotNull(message = "The 'customer' shouldn't be null")
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany
    List<Transaction> transactionHistories;

    public Account(Customer customer, BigDecimal balance) {
        this.balance = balance;
        this.customer = customer;
    }

    public Account(Long id, BigDecimal balance, Customer customer) {
        this.id = id;
        this.balance = balance;
        this.customer = customer;
    }
}
