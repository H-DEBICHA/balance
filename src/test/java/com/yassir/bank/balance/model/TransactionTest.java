package com.yassir.bank.balance.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    private Validator validator;
    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Create sample accounts for relationship checks
        fromAccount = new Account(1L, BigDecimal.valueOf(1000.00), new Customer(1L, "John Doe"));
        toAccount = new Account(2L, BigDecimal.valueOf(2000.00), new Customer(2L, "Jane Doe"));
    }

    @Test
    void testTransactionCreationWithValidData() {
        Transaction transaction = new Transaction(
                null,
                fromAccount,
                toAccount,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(900.00),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(2100.00),
                LocalDateTime.now()
        );

        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertEquals(0, violations.size(), "Expected no constraint violations with valid data");
    }

    @Test
    void testTransactionCreationWithNullFromAccount() {
        Transaction transaction = new Transaction(
                null,
                null,
                toAccount,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(900.00),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(2100.00),
                LocalDateTime.now()
        );

        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertFalse(violations.isEmpty(), "Expected constraint violations when fromAccount is null");

        ConstraintViolation<Transaction> violation = violations.iterator().next();
        assertEquals("The 'fromAccount' shouldn't be null", violation.getMessage());
    }

    @Test
    void testTransactionCreationWithNullToAccount() {
        Transaction transaction = new Transaction(
                null,
                fromAccount,
                null,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(900.00),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(2100.00),
                LocalDateTime.now()
        );

        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertFalse(violations.isEmpty(), "Expected constraint violations when toAccount is null");

        ConstraintViolation<Transaction> violation = violations.iterator().next();
        assertEquals("The 'toAccount' shouldn't be null", violation.getMessage());
    }

    @Test
    void testTransactionCreationWithNullAmount() {
        Transaction transaction = new Transaction(
                null,
                fromAccount,
                toAccount,
                null,
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(900.00),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(2100.00),
                LocalDateTime.now()
        );

        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertFalse(violations.isEmpty(), "Expected constraint violations when amount is null");

        ConstraintViolation<Transaction> violation = violations.iterator().next();
        assertEquals("The 'amount' shouldn't be null", violation.getMessage());
    }

    @Test
    void testTransactionCreationWithNullTimestamp() {
        Transaction transaction = new Transaction(
                null,
                fromAccount,
                toAccount,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(900.00),
                BigDecimal.valueOf(2000.00),
                BigDecimal.valueOf(2100.00),
                null
        );

        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        assertFalse(violations.isEmpty(), "Expected constraint violations when timestamp is null");

        ConstraintViolation<Transaction> violation = violations.iterator().next();
        assertEquals("The 'timestamp' shouldn't be null", violation.getMessage());
    }
}