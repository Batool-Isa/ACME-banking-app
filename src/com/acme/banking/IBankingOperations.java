package com.acme.banking;

public interface IBankingOperations {
    boolean login(String username, String pass);
    double deposit(Account acc, double amount, Integer transferId, String doneBy);
    double withdraw(Account acc, double amount, Integer transferId);
    void transferMoney(double amount, int srcAccount , int destinationAccount);
}
