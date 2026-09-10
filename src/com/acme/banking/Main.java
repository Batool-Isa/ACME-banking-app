package com.acme.banking;

import javax.swing.*;
import java.util.*;
import java.util.Scanner;

public class Main {

    public static void startMenu(Scanner scan, User user) {
        System.out.println("====== Welcome " + user.getFullName() + " ======");
        System.out.println("Choose the operation you want to do:");
        System.out.println("1- Create new banking account");
        System.out.println("2- Deposit Money");
        System.out.println("3- Withdraw Money");
        System.out.println("3- Transfer Money");
        System.out.println("5- Exit");
        String userInput = scan.nextLine().toLowerCase();
        switch (userInput) {
            case "1":
            case "account":
            case "create":
                createAccountMenu(scan, user);
                break;
            case "2":
            case "deposit":
                System.out.println("Choose the account you want to deposit money to.");
                ArrayList<Account> accounts = Bank.getCustomerAccounts(user);
                for (Account acc : accounts) {
                    System.out.println("Account ID: " + acc.getAccountId() +
                            " Account Type: " + acc.getType());
                }
                System.out.println("Enter the account ID:");
                int accountId = scan.nextInt();
                System.out.println("Enter the amount you want to deposit:");
                int amount = scan.nextInt();
                Account account = ((Customer) user).getAccountById(accountId);
                if (account == null) {
                    System.out.println("Invalid Account ID!!!!");
                    break;
                }
                user.deposit(account, amount);
                break;

            case "5":
            case "logout":
                return;

        }
    }


    public static Account createAccountMenu(Scanner scan, User user) {
        System.out.println("==== Choose Account Type: ====");
        System.out.println("A- Checking");
        System.out.println("B- Saving");
        System.out.println("Enter your Choice :");
        String input = scan.nextLine().toLowerCase();
        String type = Account.validateAccountType(input);
        Account acc = Account.createAccount(type, user);

        System.out.println("New " + type + " Account Created Successfully! \n" +
                "Your Account Number: " + acc.getAccountId());
        System.out.println("Please create a password for this account.");
        String pass = scan.nextLine();
        acc.setPassword(SecurityUtil.hashPassword(pass));
        acc.setFirstLogin(false);
        return acc;
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
                    User user = bank.login(username, pass);
                    if (user != null) {
                        System.out.println("Logged in successfully");
                        exitProgram = true;
                        startMenu(scan, user);
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
                    exitProgram = true;
                    System.exit(0);
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        } while (!exitProgram);


    }

}


