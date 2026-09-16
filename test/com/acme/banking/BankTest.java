package com.acme.banking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class BankTest {
    Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
    }

    @Test
    @DisplayName("Should add user to bank app list")
    public final void addUserToListsShouldAddUserToBankLists() {
        Customer customer = new Customer("Mena", "Ahmed", "Mena11", "123123123", "Customer");
        int oldArraySize = bank.getAppUsers().size();
        bank.addUserToLists(customer);
        int newArraySize = bank.getAppUsers().size();
        assertEquals(oldArraySize + 1, newArraySize);
    }



}