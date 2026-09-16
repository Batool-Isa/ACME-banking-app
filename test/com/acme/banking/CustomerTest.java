package com.acme.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;
    Account acc1;
    Bank bank;
    @BeforeEach
    public void setUp() {
        bank = new Bank();
        customer = new Customer("Esraa", "Abbas", "esraa11", "123123123", "Customer");
        acc1 = Account.createAccount("Saving", customer);
        bank.addUser(customer);
        acc1.setBalance(100);
    }

    @Test
    @DisplayName("Should return a customer object with account created")
    public final void createCustomershouldReturnACustomerWithNewAccount() {
        assertNotNull(customer);
        assertNotNull(acc1);

        // accounts should be 1
        assertEquals(1, customer.getAccounts().size());
        assertEquals("Saving", customer.getAccounts().get(0).getType());
    }

    @Test
    @DisplayName("Should maximum have 3 accounts")
    public final void customerCanOnlyCreateThreeAccount() {
        Account acc2 = Account.createAccount("Checking", customer);
        Account acc3 = Account.createAccount("Checking", customer);
        assertEquals(3, customer.getAccounts().size());
    }

    @Test
    @DisplayName("Should not be able to create 4th account")
    public final void customerCannotCreatefourthAccounts() {
        Account acc2 = Account.createAccount("Checking", customer);
        Account acc3 = Account.createAccount("Checking", customer);
        Account acc4 = Account.createAccount("Checking", customer);
        assertEquals(3, customer.getAccounts().size());
    }

    @Test
    @DisplayName("Deposit function should add amount to balance")
    public final void depositShoouldAddAmountToBalance() {
        double oldBalance = acc1.getBalance();
        customer.deposit(acc1, 90, null, customer.getFullName(), bank);
        assertEquals(oldBalance + 90, acc1.getBalance());
    }

    @Test
    @DisplayName("Withdraw function should subtract amount from balance")
    public final void withdrawShouldSubtractAmountFromBalance() {
        double oldBalance = acc1.getBalance();
        customer.withdraw(acc1, 50, null, bank);
        assertEquals(oldBalance - 50, acc1.getBalance());
    }
    @Test
    @DisplayName("Deposit should not allow zero or negative amount")
    public final void depositShouldNotAcceptZeroOrNegativeAmount() {
        double oldBalance = acc1.getBalance();
        customer.deposit(acc1, -2, null,customer.getFullName(), bank);
        assertEquals(oldBalance, acc1.getBalance());
    }
    @Test
    @DisplayName("Withdraw should not allow zero or negative amount")
    public void withdrawShouldNotAcceptZeroOrNegativeAmount() {
        double oldBalance = acc1.getBalance();
        customer.withdraw(acc1, 0, null, bank);
        assertEquals(oldBalance, acc1.getBalance());
    }


}