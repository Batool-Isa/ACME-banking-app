package com.acme.banking;

import java.io.*;
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
        intializedUsersData();
    }


    private static void intializedUsersData() {
        File file = new File("data/users.txt");
        if (file.length() == 0) {
            loadInitialUser();
        }else{
            loadUsers();
        }

    }

    private static void loadInitialUser() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/init.txt"));
            String line = new String();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 5) {
                    System.out.println("Invalid Data!!");
                    break;
                }

                switch (data[4]) {
                    case "B":
                        Banker banker = new Banker(
                                data[0],
                                data[1],
                                data[2],
                                data[3],
                                "B"
                        );
                        addUser(banker);
                        break;
                    case "C":
                        Customer customer = new Customer(
                                data[0],
                                data[1],
                                data[2],
                                data[3],
                                "C"
                        );
                        addUser(customer);
                        break;

                    default:
                        System.out.println("Invalid role");
                }

            }

            System.out.println(appUsers);
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
                    .add(user.getPassword()).add(user.getRole().substring(0)).toString();


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
            String line = new String();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("//|");

                if (data.length != 6) {
                    System.out.println("Invalid Data");
                    System.out.println("Sfds");
                    break;
                }

                switch (data[5]) {
                    case "B":
                        Banker banker = new Banker(
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                "B"
                        );
                        banker.setUserId(Integer.parseInt(data[0]));
                        addUserToLists(banker);
                        break;
                    case "C":
                        Customer customer = new Customer(
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                "C"
                        );
                        customer.setUserId(Integer.parseInt(data[0]));
                        addUserToLists(customer);
                        break;

                    default:
                        System.out.println("Invalid role");
                }

            }

            System.out.println(appUsers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }    }


    public boolean login(String username, String pass) {

        for (User user : appUsers) {

            System.out.println("Entered username: " + username);
            System.out.println("Stored username: " + user.getUsername());

            System.out.println("Entered password: " + pass);
            System.out.println("Stored hash: " + user.getPassword());

            System.out.println("Password matches: " + user.checkPassword(pass));

            if (user.getUsername().equals(username)
                    && user.checkPassword(pass)) {
                return true;
            }
        }

        return false;


    }
}
