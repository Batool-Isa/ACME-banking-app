package com.acme.banking;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Account {
    private int accountId;
    private String type;
    private String password;
    private double balance;
    private LocalDate createdAt;
    private int OverDraftCounter;
    private boolean isActive;
    private boolean firstLogin;
    static int startId = 9000;

    public Account(String type, double balance) {
        startId++;
        this.accountId = startId;
        this.type = type;
        this.password = "SDFdf";
        this.balance = balance;
        this.createdAt = LocalDate.now();
        OverDraftCounter = 0;
        this.isActive = true;
        this.firstLogin = false;
    }

    public static void setStartId(int id) {
        startId = id;
    }
    public int getOverDraftCounter() {
        return OverDraftCounter;
    }

    public void setOverDraftCounter(int overDraftCounter) {
        this.OverDraftCounter = overDraftCounter;
    }

    public static int getStartId() {
        return startId;
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
        modifyAccountValue(balance, accountId);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public static Account createAccount(String type, User user) {
        Account acc = new Account(validateAccountType(type), 0);
        ((Customer) user).addAccount(acc);
        saveAccountsToFile(user, acc);
        return new Account(type, 0);
    }

    public static String validateAccountType(String type) {
        type = type.toLowerCase();
        if (!type.equals("a") &&
                !type.equals("saving") &&
                !type.equals("b") &&
                !type.equals("checking")) {
            System.out.println("Invalid Input !! please try again");
        }
        if (type.equals("a") || type.equals("checking")) {
            return "Checking";
        } else {
            return "Saving";
        }
    }


    public static void saveAccountsToFile(User user, Account acc) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/accounts.txt", true));
            Customer customer = (Customer) user;
            String linesToAppend = String.valueOf(new StringJoiner("|")
                    .add(String.valueOf(user.getUserId()))
                    .add(String.valueOf(customer.getCustomerId()))
                    .add(String.valueOf(acc.getAccountId()))
                    .add(acc.getType())
                    .add(acc.getPassword())
                    .add(String.valueOf(acc.getBalance()))
                    .add(String.valueOf(acc.getCreatedAt()))
                    .add(String.valueOf(acc.getOverDraftCounter())));

            writer.write(linesToAppend);
            writer.newLine();

            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifyAccountValue(double balance, int accountId) {

        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader("data/accounts.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] values = line.split("\\|");

                if (values[2].equals(String.valueOf(accountId))) {
                    values[5] = String.valueOf(balance);
                    line = String.join("|", values);
                }

                lines.add(line);
            }

            reader.close();

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter("data/accounts.txt"));

            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }

            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
