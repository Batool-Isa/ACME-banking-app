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
        Account account = Account.createAccount(accType, customer);
        account.setFirstLogin(true);
        System.out.println("new account id: " + account.getAccountId());
        return customer;
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);

    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getAccountById(int accId) {
        return accounts.stream().filter(acc -> acc.getAccountId() == accId).findFirst().orElse(null);
    }

    @Override
    public double deposit(Account acc, double amount) {
        System.out.println("user balance before seposit: "+acc.getBalance());

        if (amount > 0) {
            double balance = acc.getBalance() + amount;
            acc.setBalance(balance);
            System.out.println("Amount deposit successfully. Your Balance for Account " + acc.getAccountId() + " :" + acc.getBalance());
            return balance;
        }
        return -1;
    }

    @Override
    public void withdraw(double amount) {

    }

    @Override
    public void transferMoney(double amount, int srcAccount, int destinationAccount) {

    }
}
