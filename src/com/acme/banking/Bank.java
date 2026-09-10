package com.acme.banking;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.StringJoiner;

public class Bank {

    private static ArrayList<User> appUsers;
    private static ArrayList<Customer> customerArrayList;
    private static ArrayList<Banker> bankersList;

    public Bank() {
        appUsers = new ArrayList<>();
        customerArrayList = new ArrayList<>();
        bankersList = new ArrayList<>();
        //load bank users
        initializedUsersData();
        loadAccounts();
    }


    public static ArrayList<User> getAppUsers() {
        return appUsers;
    }

    private static void initializedUsersData() {
        File file = new File("data/users.txt");
        if (file.length() == 0) {
            loadInitialUser();
        } else {
            loadUsers();
        }

    }

    private static void loadInitialUser() {
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

    public static void addUserToLists(User user) {
        appUsers.add(user);
        if (user instanceof Customer) {
            customerArrayList.add((Customer) user);

        } else {
            bankersList.add((Banker) user);
        }
    }

    public static void addUser(User user) {
        addUserToLists(user);
        addUsersToFile(user);
    }

    public static void addUsersToFile(User user) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", true));
            String linesToAppend = new StringJoiner("|")
                    .add(String.valueOf(user.getUserId()))
                    .add(user.getFirstName())
                    .add(user.getLastName())
                    .add(user.getUsername())
                    .add(user.getPassword())
                    .add(user.getRole()).toString();


            writer.write(linesToAppend);
            writer.newLine();

            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadUsers() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length != 6) {
                    System.out.println("Invalid Data");
                    break;
                }

                switch (data[5]) {
                    case "Banker":
                        Banker banker = new Banker(data[1], data[2], data[3], "", "Banker");
                        banker.setUserId(Integer.parseInt(data[0]));
                        banker.setPassword(data[4]);
                        addUserToLists(banker);
                        break;
                    case "Customer":
                        Customer customer = new Customer(data[1], data[2], data[3], "", "Customer");
                        customer.setUserId(Integer.parseInt(data[0]));
                        customer.setPassword(data[4]);
                        addUserToLists(customer);
                        break;

                    default:
                        System.out.println("Invalid role");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public User login(String username, String pass) {
        for (User user : appUsers) {
            if (user.getUsername().equals(username) && user.checkPassword(pass)) {

                return user;
            }
        }
        return null;
    }

    public static void loadAccounts() {

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

                Customer customer = getCustomerById(userId);

                if (customer != null) {
                    Account account = new Account(
                            type,
                            balance
                    );
                    account.setAccountId(accountId);
                    account.setPassword(password);
                    account.setCreatedAt(createdAt);
                    account.setOverDraftCounter(overDraftCounter);

                    customer.addAccount(account);
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
    public static ArrayList<Account> getCustomerAccounts(User user) {
        if (user instanceof Customer) {
            System.out.println(((Customer) user).getAccounts());
            return ((Customer) user).getAccounts();
        }
        return null;
    }

    public static Account getAccountById(int id, User user){
        if(user instanceof Customer){
            Customer cus = (Customer) user;
            for (Account acc: cus.getAccounts()){
                if(acc.getAccountId() == id){
                    return acc;
                }
            }
        }
        return null;
    }

    public static Customer getCustomerById(int userId) {
        return customerArrayList.stream().filter(cus -> cus.getUserId() == userId).findFirst().orElse(null);

    }
}
