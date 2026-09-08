package com.acme.banking;

public class Customer extends User{
    private int customerId;
    private static int idStart = 5000;
    public Customer(String firstName, String lastName, String username, String password, String role) {
        super(firstName, lastName, username, password,role);
        idStart++;
        this.customerId = idStart;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void deposit(double amount) {

    }

    @Override
    public void withdraw(double amount) {

    }

    @Override
    public void transferMoney(double amount, int srcAccount, int destinationAccount) {

    }
}
