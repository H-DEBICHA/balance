package com.yassir.bank.balance.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

class AccountTest {
    private Validator validator;
    private Customer customer;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Creating a sample customer for the relationship check
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
    }

    @Test
    void testAccountCreationWithValidData() {
        Account account = new Account(null, BigDecimal.valueOf(1000.00), customer);

        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        assertEquals(0, violations.size(), "Expected no constraint violations with valid data");
    }

    @Test
    void testAccountCreationWithNullBalance() {
        Account account = new Account(null, null, customer);

        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        assertFalse(violations.isEmpty(), "Expected constraint violations when balance is null");

        ConstraintViolation<Account> violation = violations.iterator().next();
        assertEquals("The 'balance' shouldn't be null", violation.getMessage());
    }

    @Test
    void testAccountCreationWithNullCustomer() {
        Account account = new Account(null, BigDecimal.valueOf(1000.00), null);

        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        assertFalse(violations.isEmpty(), "Expected constraint violations when customer is null");

        ConstraintViolation<Account> violation = violations.iterator().next();
        assertEquals("The 'customer' shouldn't be null", violation.getMessage());
    }

    @Test
    void testAccountBalancePrecisionAndScale() {
        Account account = new Account(null, BigDecimal.valueOf(123456789012345.67), customer);

        Set<ConstraintViolation<Account>> violations = validator.validate(account);
        assertEquals(0, violations.size(), "Expected no constraint violations with valid balance precision and scale");
    }
}