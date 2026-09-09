package com.acme.banking;

import java.util.ArrayList;

public class Customer extends User {
    private int customerId;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactionsList = new ArrayList<>();
    private int idStart = 5000;

    public Customer(String firstName, String lastName, String username, String password, String role) {
        super(firstName, lastName, username, password, role);
        idStart++;
        this.customerId = idStart;
        this.accounts = new ArrayList<>();
        this.transactionsList = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public static Customer createCustomer(String firstName, String lastName, String username, String password, String accType) {
        Customer customer = new Customer(firstName, lastName, username, password, "Customer");
        Account account = Account.createAccount(accType);
        System.out.println("new account id: "+account.getAccountId());
        customer.addAccount(account);
        return customer;
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);
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
