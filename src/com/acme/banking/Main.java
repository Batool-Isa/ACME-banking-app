package com.acme.banking;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;
import java.util.Scanner;

public class Main {

    public static void startMenu(Scanner scan) {
        System.out.println("====== Welcome to ACME Banking App! ======");
        System.out.println("Choose the operation you want to do:");
        System.out.println("1- Create new banking account");
        System.out.println("2- Deposit Money");
        System.out.println("3- Withdraw Money");
        System.out.println("3- Transfer Money");
        System.out.println("5- Logout");
        System.out.println("6- Exit");
        String userInput = scan.nextLine().toLowerCase();
        switch (userInput) {
            case "1":
            case "account":
            case "create":
                Account.createAccountMenu(scan);

        }
    }


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Bank bank = new Bank();
        boolean exitProgram = false;
        do {
            System.out.println("====== Welcome to ACME Banking App! ======");
            System.out.println("Choose the operation you want to do:");
            System.out.println("1- Login");
            System.out.println("2- SignUp");
            System.out.println("3- Exit");
            String userInput = scan.nextLine().toLowerCase();

            switch (userInput) {
                case "1":
                case "login":
                    System.out.println("Enter Username:");
                    String username = scan.nextLine();
                    System.out.println("Enter password");
                    String pass = scan.nextLine();
                    System.out.println();
                    if (bank.login(username, pass)) {
                        System.out.println("Logged in successfully");
                        startMenu(scan);
                    } else {
                        System.out.println("Invalid username or password, please try again!");
                    }
                    break;
                case "2":
                case "sign up":
                    System.out.println("Enter Username:");
                    String customerUsername = scan.nextLine();
                    System.out.println("Enter your first name:");
                    String firstName = scan.nextLine();
                    System.out.println("Enter your last name:");
                    String lastName = scan.nextLine();
                    System.out.println("Enter password");
                    String password = scan.nextLine();
                    System.out.println("Choose Account Type \n"
                            + "A- Checking \n"
                            + "B- Saving");
                    String type = scan.nextLine().toLowerCase();
                    Customer customer = Customer.createCustomer(firstName, lastName, customerUsername, password, type);
                    bank.addUser(customer);
                    System.out.println("Congrats, Your Banking Account Created Successfully!");
                    System.out.println("Your username: " + customer.getUsername() + " & customer Id: " + customer.getCustomerId());
                    break;
                case "3":
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        } while (!exitProgram);


    }

}


