package com.acme.banking;

import java.util.*;
import java.util.Scanner;

public class Main {

    public static void startMenu(Scanner scan, User user) {
        boolean continueMenu = true;
        while(continueMenu){
            System.out.println("");
            System.out.println("====== Welcome " + user.getFullName() + "! ======");
            System.out.println("Choose the operation you want to do:");
            System.out.println("1- Create new banking account");
            System.out.println("2- Deposit Money");
            System.out.println("3- Withdraw Money");
            System.out.println("4- Transfer Money");
            System.out.println("5- Exit");
            String userInput = scan.nextLine().toLowerCase();
            ArrayList<Account> accounts = Bank.getCustomerAccounts(user);
            Customer customer = Bank.getCustomerById(user.getUserId());
            switch (userInput) {
                case "1":
                case "account":
                case "create":
                    createAccountMenu(scan, user);
                    break;
                case "2":
                case "deposit":
                    System.out.println("Choose the account you want to deposit money to.");
                    for (Account acc : accounts) {
                        System.out.println("Account ID: " + acc.getAccountId() +
                                " Account Type: " + acc.getType());
                    }
                    boolean valid = false;
                    do {
                        System.out.print("Enter the account ID:");
                        int accountId = scan.nextInt();
                        Account account = (customer.getAccountById(accountId));
                        if (account != null) {
                            System.out.print("Enter the amount you want to deposit:");
                            int amount = scan.nextInt();
                            user.deposit(account, amount);
                            valid = true;
                        } else {
                            System.out.print("Invalid Account ID!!!!");
                        }
                    } while (!valid);
                    scan.nextLine();
                    break;
                case "3":
                case "withdraw":
                    System.out.println("Choose the account you want to withdraw money to.");
                    for (Account acc : accounts) {
                        System.out.println("Account ID: " + acc.getAccountId() +
                                " Account Type: " + acc.getType());
                    }
                    System.out.print("Enter the account ID:");
                    int accountId2 = scan.nextInt();
                    System.out.print("Enter the amount you want to withdraw:");
                    int amount2 = scan.nextInt();
                    Account account2 = ((Customer) user).getAccountById(accountId2);
                    if (account2 == null) {
                        System.out.println("Invalid Account ID!!!!");
                        break;
                    }
                    user.withdraw(account2, amount2);
                    break;
                case "4":
                case "transfer":
                    System.out.println("Choose the account you want to transfer money from:");
                    for (Account acc : accounts) {
                        System.out.println("Account ID: " + acc.getAccountId() +
                                " Account Type: " + acc.getType());
                    }
                    boolean validId = false;
                    do {
                        System.out.print("Enter the account ID:");
                        int srcAccountId = scan.nextInt();
                        Account srcAccount = (customer.getAccountById(srcAccountId));
                        if (srcAccount != null) {
                            System.out.println("Enter the recipient account ID:");
                            int recipientAccId = scan.nextInt();
                            Customer recipientCustomer =
                                    Bank.getCustomerByAccountId(recipientAccId);

                            if (recipientCustomer == null) {
                                System.out.println("Invalid recipient account ID!!!!");
                                continue;
                            }
                            System.out.println("Enter the amount you want to transfer:");
                            int transferAmount = scan.nextInt();
                            user.transferMoney(transferAmount, srcAccountId,recipientAccId);
                            validId = true;
                        } else {
                            System.out.println("Invalid Account ID!!!!");
                        }
                    } while (!validId);
                    scan.nextLine();
                    break;
                case "5":
                case "logout":
                    continueMenu= false;
                    return;

            }
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
                    System.out.print("Enter Username: ");
                    String username = scan.nextLine();
                    System.out.print("Enter password: ");
                    String pass = scan.nextLine();
                    System.out.println();
                    User user = bank.login(username, pass);
                    if (user != null) {
                        System.out.println("Logged in successfully");
                        startMenu(scan, user);
                    } else {
                        System.out.println("Invalid username or password, please try again!");
                    }
                    break;
                case "2":
                case "sign up":
                    // add input validation later
                    System.out.print("Enter Username:");
                    String customerUsername = scan.nextLine().trim();
                    System.out.print("Enter your first name:");
                    String firstName = scan.nextLine().trim();
                    System.out.print("Enter your last name:");
                    String lastName = scan.nextLine().trim();
                    System.out.print("Enter password");
                    String password = scan.nextLine();
                    System.out.print("Choose Account Type \n"
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


