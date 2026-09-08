package com.acme.banking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Bank {

    private static ArrayList<User> appUsers;
    private static ArrayList<Customer> customerArrayList;
    private static ArrayList<Banker> bankersList;

    public Bank() {
        appUsers = new ArrayList<>();
        customerArrayList = new ArrayList<>();
        bankersList = new ArrayList<>();
    }

    public static void addUser(User user) {
        appUsers.add(user);
        if (user instanceof Customer) {
            customerArrayList.add((Customer) user);

        } else {
            bankersList.add((Banker) user);
        }
        addUsersToFile(user);

    }

    public static void addUsersToFile(User user) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", true));
            String linesToAppend = new StringJoiner(",")
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
            BufferedReader reader = new BufferedReader(new FileReader("data/initalUsers.txt"));
            String line = new String();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 5) {
                    System.out.println("Invalid Data");
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


}
