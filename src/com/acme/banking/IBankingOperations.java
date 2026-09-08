package com.acme.banking;

public interface IBankingOperations {
    boolean login(String username, String pass);
    void deposit(double amount);
    void withdraw(double amount);
    void transferMoney(double amount, int srcAccount , int destinationAccount);
}
