package com.acme.banking;

import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;


public class Account {
    private int accountId;
    private String type;
    private double balance;
    private LocalDate createdAt;
    private int OverDraftCounter;
    private boolean isActive;
    int startId = 9000;

    public Account(String type, double balance) {
        startId++;
        this.accountId = startId;
        this.type = type;
        this.balance = balance;
        this.createdAt = LocalDate.now();
        OverDraftCounter = 0;
        this.isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getOverDraftCounter() {
        return OverDraftCounter;
    }

    public void setOverDraftCounter(int overDraftCounter) {
        OverDraftCounter = overDraftCounter;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public static Account createAccount(String type) {
        return new Account(type, 0);
    }
    public static String validateAccountType(String type){
        if (!type.equals("a") &&
                !type.equals("saving") &&
                !type.equals("b") &&
                !type.equals("checking")) {
            System.out.println("Invalid Input !! please try again");
        }

        if (type.equals("a") || type.equals("checking")) {
            return  "Checking";
        } else {
            return  "Saving";
        }
    }
    public static Account createAccountMenu(Scanner scan) {
        System.out.println("==== Choose Account Type: ====");
        System.out.println("A- Checking");
        System.out.println("B- Saving");
        System.out.println("Enter your Choice :");
        String input = scan.nextLine().toLowerCase();

        String type = validateAccountType(input);

        Account acc = createAccount(type);
        System.out.println("New " + type + " Account Created Successfully! /n" +
                "Your Account Number: " + acc.getAccountId());
return acc;
    }

}
