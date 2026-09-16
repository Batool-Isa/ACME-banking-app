package com.acme.banking;

public interface IBankingOperations {
    void deposit(Account acc, double amount, Integer transferId, String doneBy, Bank bank);
    void withdraw(Account acc, double amount, Integer transferId, Bank bank);
    void transferMoney(double amount, int srcAccount , int destinationAccount, Bank bank);
}
