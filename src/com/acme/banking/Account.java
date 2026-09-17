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
        //this.password = SecurityUtil.hashPassword(password);
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
        // if user has 2 overdraft or more , deactivate its account
        if (this.OverDraftCounter >= 2) {
            System.out.println(ConsoleColors.RED
            +" Your account has been deactivated after reaching the overdraft limit."+ConsoleColors.RESET);
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
        modifyAccountValue(this);
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
        modifyAccountValue(this);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        //update account in accounts file
        modifyAccountValue(this);
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public static Account createAccount(String type, User user) {
        Customer customer = (Customer) user;
        if(customer.getAccounts().size() >= 3){
            System.out.println();
            System.out.println(ConsoleColors.RED +
                    "You cannot have more than 3 accounts." +
                    ConsoleColors.RESET);
            System.out.println();

            return null;
        }
        Card card = new Card(Card.CardType.MASTERCARD);
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
            System.out.println(ConsoleColors.RED +
                    "Invalid account type. Please try again." +
                    ConsoleColors.RESET);
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
                    .add(String.valueOf(acc.getCard().getCvv())));

            writer.write(linesToAppend);
            writer.newLine();

            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifyAccountValue(Account account) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader("data/accounts.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] values = line.split("\\|");

                if (values[2].equals(String.valueOf(account.getAccountId()))) {
                    values[4] = String.valueOf(account.getPassword());
                    values[5] = String.valueOf(account.getBalance());
                    values[10] = String.valueOf(account.getCard().getType());

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
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== CHANGE CARD TYPE ==========" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.YELLOW +
                "Current Card Type: " + card.getType() +
                ConsoleColors.RESET);
        ArrayList<Card.CardType> types = Arrays.stream(Card.CardType.values())
                .filter(t -> !t.equals(acc.getCard().getType()))
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println();
        System.out.println("Available Card Types: " + types);
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                "Enter the new card type: " +
                ConsoleColors.RESET);

        String input = scan.nextLine();
        if (input.equalsIgnoreCase(String.valueOf(Card.CardType.MASTERCARD))) {
            acc.getCard().setType(Card.CardType.MASTERCARD);
        } else if (input.equalsIgnoreCase(String.valueOf(Card.CardType.MASTERCARD_PLATINUM))) {
            acc.getCard().setType(Card.CardType.MASTERCARD_PLATINUM);
        } else if (input.equalsIgnoreCase(String.valueOf(Card.CardType.MASTERCARD_TITANIUM))) {
            acc.getCard().setType(Card.CardType.MASTERCARD_TITANIUM);
        }
        modifyAccountValue(acc);
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.GREEN +
                "Your new Card Type: " + card.getType() +
                ConsoleColors.RESET);
        System.out.println();
    }
    public static void viewCardDetails(Scanner scan, Customer customer,
                                       ArrayList<Account> accounts) {

        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== CARD DETAILS ==========" +
                ConsoleColors.RESET);
        System.out.println();

        customer.printAccount(accounts);

        System.out.println();

        System.out.print(ConsoleColors.YELLOW +
                "Enter the account ID: " +
                ConsoleColors.RESET);

        int accountId = scan.nextInt();
        scan.nextLine();

        Account account = customer.getAccountById(accountId);

        if (account == null) {
            System.out.println(ConsoleColors.RED +
                    "Account not found." +
                    ConsoleColors.RESET);
            return;
        }

        Card card = account.getCard();

        System.out.println();
        System.out.println("Account ID   : " + account.getAccountId());
        System.out.println("Card Type    : " + card.getType());
        System.out.println("Card Number  : " + card.getCardNumber());
        System.out.println("Issued Date  : " + card.getDateIssued());
        System.out.println("Expiry Date  : " + card.getExpiryDate());
    }
}
