package com.acme.banking;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.StringJoiner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bank {

    private ArrayList<User> appUsers;
    private ArrayList<Customer> customerArrayList;
    private ArrayList<Banker> bankersList;

    public Bank() {
        appUsers = new ArrayList<>();
        customerArrayList = new ArrayList<>();
        bankersList = new ArrayList<>();
        //load bank users
        initializedUsersData();
        loadAccounts();
        loadTransactions();
    }


    private  void initializedUsersData() {
        File file = new File("data/users.txt");
        if (file.length() == 0) {
            loadInitialUser();
        } else {
            loadUsers();
        }

    }

    public boolean checkUsername(String username){
        return this.getAppUsers().stream()
                .anyMatch(u ->u.getUsername().equalsIgnoreCase(username));
    }
    private  void loadInitialUser() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/init.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 5) {
                    System.out.println("Invalid Data!!");
                    break;
                }

                switch (data[4]) {
                    case "Banker":
                        Banker banker = new Banker(data[0], data[1], data[2], data[3], "Banker");
                        addUser(banker);
                        break;
                    case "Customer":
                        Customer customer = new Customer(data[0], data[1], data[2], data[3], "Customer");
                        addUser(customer);
                        break;

                    default:
                        System.out.println("Invalid role");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public  void addUserToLists(User user) {
        appUsers.add(user);
        if (user instanceof Customer) {
            customerArrayList.add((Customer) user);

        } else if (user instanceof Banker) {
            bankersList.add((Banker) user);
        }
    }

    public  void addUser(User user) {
        addUserToLists(user);
        addUsersToFile(user);
    }

    public ArrayList<User> getAppUsers() {
        return appUsers;
    }

    public  void addUsersToFile(User user) {

        try {
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter("data/users.txt", true));

            String linesToAppend;

            if (user instanceof Customer) {

                Customer customer = (Customer) user;

                linesToAppend = new StringJoiner("|")
                        .add(String.valueOf(user.getUserId()))
                        .add(String.valueOf(customer.getCustomerId()))
                        .add(user.getFirstName())
                        .add(user.getLastName())
                        .add(user.getUsername())
                        .add(user.getPassword())
                        .add(String.valueOf(customer.isFirstLogin()))
                        .add(user.getRole())
                        .toString();

            } else if (user instanceof Banker) {

                Banker banker = (Banker) user;

                linesToAppend = new StringJoiner("|")
                        .add(String.valueOf(user.getUserId()))
                        .add(String.valueOf(banker.getBankerId()))
                        .add(user.getFirstName())
                        .add(user.getLastName())
                        .add(user.getUsername())
                        .add(user.getPassword())
                        .add(user.getRole())
                        .toString();

            } else {
                throw new IllegalArgumentException("Unknown user type");
            }

            writer.write(linesToAppend);
            writer.newLine();
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  void loadUsers() {

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader("data/users.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|");

                String role = data[data.length - 1];
                int userId = Integer.parseInt(data[0]);
                if (role.equalsIgnoreCase("Banker")) {
                    if (data.length != 7) {
                        System.out.println("Invalid Data");
                    }
                    int bankerId = Integer.parseInt(data[1]);
                    Banker banker = new Banker(
                            data[2],
                            data[3],
                            data[4],
                            "",
                            "Banker"
                    );

                    banker.setUserId(userId);
                    banker.setPassword(data[5]);
                    banker.setBankerId(bankerId);

                    addUserToLists(banker);
                } else {
                    if (data.length != 8) {
                        System.out.println("Invalid Data");
                    }
                    int customerId = Integer.parseInt(data[1]);

                    Customer customer = new Customer(
                            data[2],
                            data[3],
                            data[4],
                            "",
                            "Customer"
                    );

                    customer.setUserId(userId);
                    customer.setCustomerId(customerId);
                    customer.setPassword(data[5]);
                    customer.setFirstLogin(Boolean.parseBoolean(data[data.length - 2]));
                    this.addUserToLists(customer);
                }


            }

            reader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String username, String pass, Scanner scan) {
        for (User user : appUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                if (user.checkPassword(pass)) {
                    if (user instanceof Customer && ((Customer) user).isFirstLogin()) {
                        Customer customer = getcustomerbyUserId(user.getUserId());
                        System.out.println("Login first:+ " + customer.isFirstLogin());
                        System.out.println("Your Password is temporary \n Please change the password:");
                        String password = scan.nextLine();
                        String hashedPassword = SecurityUtil.hashPassword(password);
                        user.setPassword(hashedPassword);
                        customer.setFirstLogin(false);
                        Bank.updateUserInFile(customer);
                    }
                    return user;
                } else {
                    user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                    System.out.println("Attemp: " + user.getFailedLoginAttempts());
                    if (user.getFailedLoginAttempts() >= 3) {
                        System.out.println("Account Locked for 1 minutes!!!");
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        user.setFailedLoginAttempts(0);
                        System.out.println("Account unlocked! Please try again.");

                    }

                }
            }
        }
        return null;
    }

    public Account getAccount(int accountId) {

        return customerArrayList.stream()
                .flatMap(c -> c.getAccounts().stream())
                .filter(a -> a.getAccountId() == accountId)
                .findFirst().orElse(null);


    }

    public  void loadAccounts() {
        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader("data/accounts.txt"));
            String line;

            while ((line = reader.readLine()) != null) {

                String[] values = line.split("\\|");

                int userId = Integer.parseInt(values[0]);
                int customerId = Integer.parseInt(values[1]);
                int accountId = Integer.parseInt(values[2]);
                String type = values[3];
                String password = values[4];
                double balance = Double.parseDouble(values[5]);
                LocalDate createdAt = LocalDate.parse(values[6]);
                int overDraftCounter = Integer.parseInt(values[7]);
                boolean isActive = Boolean.parseBoolean(values[8]);
                int cardId = Integer.parseInt(values[9]);
                Card.CardType cardType = Card.CardType.valueOf(values[10]);
                String cardNumber = values[11];

                LocalDate dateIssued = LocalDate.parse((values[12]));
                LocalDate dateExpiry = LocalDate.parse(values[13]);
                String cvv = values[14];

                Customer customer = this.getcustomerbyUserId(userId);
                if (customer != null) {
                    // Restore card
                    Card card = new Card(cardType);
                    card.setCvv(cvv);
                    card.setCardNumber(String.valueOf(cardNumber));
                    card.setDateIssued(dateIssued);
                    card.setExpiryDate(dateExpiry);
                    card.setCardId(cardId);
                    Account account = new Account(
                            type,
                            balance,
                            card
                    );
                    account.setAccountId(accountId);
                    account.setPassword(password);
                    account.setCreatedAt(createdAt);
                    account.setOverDraftCounter(overDraftCounter);
                    account.setCard(card);
                    card.setAccountId(accountId);
                    customer.addAccount(account);
                }

                if (accountId > Account.getStartId()) {
                    Account.setStartId(accountId);
                }
            }

            reader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  void loadTransactions() {

        for (Customer customer : customerArrayList) {
            File customerFile = new File(
                    "data/customerFiles",
                    "Customer-" + customer.getFullName()
                            + "-" + customer.getCustomerId()
            );

            if (!customerFile.exists()) {
                continue;
            }

            try {
                BufferedReader reader =
                        new BufferedReader(new FileReader(customerFile));

                String line;

                int accountId = 0;
                int transactionId = 0;
                Transaction.TransactionType type = null;
                LocalDateTime dateTime = null;
                double amount = 0;
                double balance = 0;
                String doneBy = "";
                Integer transferId = null;

                while ((line = reader.readLine()) != null) {

                    if (line.startsWith("Account ID: ")) {

                        accountId = Integer.parseInt(
                                line.substring("Account ID: ".length())
                        );

                    } else if (line.startsWith("Transaction ID: ")) {

                        transactionId = Integer.parseInt(
                                line.substring("Transaction ID: ".length())
                        );

                    } else if (line.startsWith("Type: ")) {

                        type = Transaction.TransactionType.valueOf(
                                line.substring("Type: ".length())
                        );

                    } else if (line.startsWith("Date: ")) {

                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        dateTime = LocalDateTime.parse(
                                line.substring("Date: ".length()),
                                formatter
                        );

                    } else if (line.startsWith("Amount: ")) {

                        amount = Double.parseDouble(
                                line.substring("Amount: ".length())
                        );

                    } else if (line.startsWith("Balance After Transaction: ")) {

                        balance = Double.parseDouble(
                                line.substring("Balance After Transaction: ".length())
                        );

                    } else if (line.startsWith("Done By: ")) {

                        doneBy = line.substring("Done By: ".length());

                    } else if (line.startsWith("Transfer ID: ")) {

                        transferId = Integer.parseInt(
                                line.substring("Transfer ID: ".length())
                        );

                    } else if (line.startsWith("--------------------------------")) {

                        Transaction trans = new Transaction(
                                type,
                                doneBy,
                                balance,
                                amount,
                                accountId,
                                transferId
                        );

                        trans.setTransactionId(transactionId);
                        trans.setDateTime(dateTime);

                        customer.addTransaction(trans);

                        accountId = 0;
                        transactionId = 0;
                        type = null;
                        dateTime = null;
                        amount = 0;
                        balance = 0;
                        doneBy = "";
                        transferId = null;
                    }
                }

                reader.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ArrayList<Account> getCustomerAccounts(User user) {
        if (user instanceof Customer) {
            //  System.out.println(((Customer) user).getAccounts());
            return ((Customer) user).getAccounts();
        }
        return null;
    }

    public static Account getAccountById(int id, User user) {
        if (user instanceof Customer) {
            Customer cus = (Customer) user;
            for (Account acc : cus.getAccounts()) {
                if (acc.getAccountId() == id) {
                    return acc;
                }
            }
        }
        return null;
    }

    public  Customer getcustomerbyUserId(int userId) {
        return customerArrayList.stream()
                .filter(cus -> cus.getUserId() == userId)
                .findFirst().orElse(null);
    }

    public Customer getCustomerByCustomerId(int customerId) {
        return customerArrayList.stream()
                .filter(cus -> cus.getCustomerId() == customerId)
                .findFirst().orElse(null);
    }

    public  Customer getCustomerByAccountId(int accountId) {

        for (Customer customer : this.customerArrayList) {

            Account account = customer.getAccountById(accountId);

            if (account != null) {
                return customer;
            }
        }

        return null;
    }

    public static void updateUserInFile(User user) {

        File file = new File("data/users.txt");
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|");

                if (Integer.parseInt(data[0]) == user.getUserId()) {

                    String updatedLine;

                    if (user instanceof Customer) {

                        Customer customer = (Customer) user;

                        updatedLine = String.join("|",
                                String.valueOf(user.getUserId()),
                                String.valueOf(customer.getCustomerId()),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getUsername(),
                                user.getPassword(),
                                String.valueOf(customer.isFirstLogin()),
                                user.getRole()
                        );

                    } else {

                        Banker banker = (Banker) user;

                        updatedLine = String.join("|",
                                String.valueOf(user.getUserId()),
                                String.valueOf(banker.getBankerId()),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getUsername(),
                                user.getPassword(),
                                user.getRole()
                        );
                    }

                    updatedLines.add(updatedLine);

                } else {
                    updatedLines.add(line);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
