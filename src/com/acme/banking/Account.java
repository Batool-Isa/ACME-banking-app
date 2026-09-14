package com.acme.banking;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Account {
    private int accountId;
    private String type;
    private String password;
    private double balance;
    private LocalDate createdAt;
    private int OverDraftCounter;
    private boolean isActive;
    private Card card;
    static int startId = 9000;

    public Account(String type, double balance, Card card) {
        startId++;
        this.accountId = startId;
        this.type = type;
        this.password = "SDFdf";
        this.balance = balance;
        this.createdAt = LocalDate.now();
        OverDraftCounter = 0;
        this.isActive = true;
        this.card = card;
    }

    public static void setStartId(int id) {
        startId = id;
    }

    public int getOverDraftCounter() {
        return OverDraftCounter;
    }

    public void setOverDraftCounter(int overDraftCounter) {
        this.OverDraftCounter = overDraftCounter;
        if (this.OverDraftCounter >= 2) {
            setActive(false);
        }
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public static Account createAccount(String type, User user) {
        Card card = new Card(Card.CardType.MASTERCARD, 900033, 123);
        Account acc = new Account(validateAccountType(type), 0, card);
        ((Customer) user).addAccount(acc);
        saveAccountsToFile(user, acc);
        return acc;
    }

    public static String validateAccountType(String type) {
        type = type.toLowerCase();
        if (!type.equals("a") &&
                !type.equals("saving") &&
                !type.equals("b") &&
                !type.equals("checking")) {
            System.out.println("Invalid Input !! please try again");
            return null;
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
                    .add(String.valueOf(acc.getOverDraftCounter()))
                    .add(String.valueOf(acc.isActive()))
                    .add(String.valueOf(acc.getCard().getCardId()))
                    .add(acc.getCard().getType().name())
                    .add(String.valueOf(acc.getCard().getCardNumber()))
                    .add(String.valueOf(acc.getCard().getDateIssued()))
                    .add(String.valueOf(acc.getCard().getExpiryDate()))
                    .add(String.valueOf(acc.getCard().getCvs())));

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
    public static void modifyAccountCard(Card.CardType type, int accountId) {

        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader("data/accounts.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] values = line.split("\\|");

                if (values[2].equals(String.valueOf(accountId))) {
                    values[10] = String.valueOf(type);
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


    public void changeCardType(Account acc, Scanner scan) {
        Card card = acc.getCard();
        System.out.println("Your Current Card Type: " + card.getType());
        ArrayList<Card.CardType> types = Arrays.stream(Card.CardType.values())
                .filter(t -> !t.equals(acc.getCard().getType()))
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Choose the new type:" + types);

        String input = scan.nextLine();
        if (input.equalsIgnoreCase(String.valueOf(Card.CardType.MASTERCARD))) {
            acc.getCard().setType(Card.CardType.MASTERCARD);
            modifyAccountCard(acc.getCard().getType(), acc.getAccountId());
        } else if (input.equalsIgnoreCase(String.valueOf(Card.CardType.MASTERCARD_PLATINUM))) {
            acc.getCard().setType(Card.CardType.MASTERCARD_PLATINUM);
            modifyAccountCard(acc.getCard().getType(), acc.getAccountId());

        } else if (input.equalsIgnoreCase(String.valueOf(Card.CardType.MASTERCARD_TITANIUM))) {
            acc.getCard().setType(Card.CardType.MASTERCARD_TITANIUM);
            modifyAccountCard(acc.getCard().getType(), acc.getAccountId());

        }
        System.out.println("Your new Card Type: " + card.getType());

    }
}
