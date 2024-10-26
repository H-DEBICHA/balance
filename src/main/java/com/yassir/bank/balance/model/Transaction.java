package com.yassir.bank.balance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The 'fromAccount' shouldn't be null")
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @NotNull(message = "The 'toAccount' shouldn't be null")
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @NotNull(message = "The 'amount' shouldn't be null")
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "The 'fromAccountOldBalance' shouldn't be null")
    @Column(name ="from_account_old_balance", precision = 19, scale = 2)
    private BigDecimal fromAccountOldBalance;

    @NotNull(message = "The 'fromAccountNewBalance' shouldn't be null")
    @Column(name ="from_account_new_balance", precision = 19, scale = 2)
    private BigDecimal fromAccountNewBalance;


    @NotNull(message = "The 'toAccountOldBalance' shouldn't be null")
    @Column(name ="to_account_old_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal toAccountOldBalance;

    @NotNull(message = "The 'toAccountNewBalance' shouldn't be null")
    @Column(name ="to_account_new_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal toAccountNewBalance;

    @NotNull(message = "The 'timestamp' shouldn't be null")
    @Column(nullable = false)
    private LocalDateTime timestamp;



}
