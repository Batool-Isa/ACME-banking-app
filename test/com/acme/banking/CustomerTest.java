package com.acme.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;
    @BeforeEach
    public void setUp() {
        customer = new Customer("Esraa","Abbas","esraa11","123123123","Customer");
    }

    @Test
    @DisplayName("Should return a customer object with account created")
    public final void createCustomershouldReturnACustomerWithNewAccount() {
        customer = new Customer("Esraa","Abbas","esraa11","123123123","Customer");
       Account acc = Account.createAccount("Saving", customer);
        assertNotNull(customer);
        // accounts should be 1
        assertEquals(1, customer.getAccounts().size());
        assertEquals("Saving", customer.getAccounts().get(0).getType());
    }


}