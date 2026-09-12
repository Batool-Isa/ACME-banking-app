package com.acme.banking;

public interface IBankingOperations {
    boolean login(String username, String pass);
    double deposit(Account acc, double amount);
    double withdraw(Account acc, double amount);
    void transferMoney(double amount, int srcAccount , int destinationAccount);
}
