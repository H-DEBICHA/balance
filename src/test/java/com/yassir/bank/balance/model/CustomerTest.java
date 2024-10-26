package com.yassir.bank.balance.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCustomerCreationWithValidName() {
        Customer customer = new Customer(null, "John Doe");

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertEquals(0, violations.size(), "Expected no constraint violations with a valid name");
    }

    @Test
    void testCustomerCreationWithBlankName() {
        Customer customer = new Customer(null, "   ");

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertFalse(violations.isEmpty(), "Expected constraint violations when name is blank");

        ConstraintViolation<Customer> violation = violations.iterator().next();
        assertEquals("The name shouldn't be blank", violation.getMessage());
    }

    @Test
    void testCustomerCreationWithNullName() {
        Customer customer = new Customer(null, null);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertFalse(violations.isEmpty(), "Expected constraint violations when name is null");

        ConstraintViolation<Customer> violation = violations.iterator().next();
        assertEquals("The name shouldn't be blank", violation.getMessage());
    }
}