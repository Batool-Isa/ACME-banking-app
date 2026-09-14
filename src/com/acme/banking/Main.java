package com.acme.banking;

import java.util.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void startMenu(Scanner scan, User user, Bank bank) {
        boolean continueMenu = true;
        while (continueMenu) {
            System.out.println("\n====== Welcome " + user.getFullName() + "! ======");
            System.out.println("Choose the operation you want to do:");
            System.out.println("1- Create new banking account");
            System.out.println("2- Deposit Money");
            System.out.println("3- Withdraw Money");
            System.out.println("4- Transfer Money");
            System.out.println("5- Print Detailed Account Statement");
            System.out.println("6- Filter Transaction");
            System.out.println("7- Change Card Type");
            System.out.println("8- Exit");
            String userInput = scan.nextLine().toLowerCase();
            ArrayList<Account> accounts = Bank.getCustomerAccounts(user);
            Customer customer = bank.getcustomerbyUserId(user.getUserId());
            switch (userInput) {
                case "1":
                case "account":
                case "create":
                    createAccountMenu(scan, user);
                    break;
                case "2":
                case "deposit":
                    System.out.println("Choose the account you want to deposit money to.");
                    customer.printAccount(accounts);
                    boolean valid = false;
                    do {
                        System.out.print("Enter the account ID:");
                        int accountId = scan.nextInt();
                        Account account = bank.getAccount(accountId);
                        if (account != null) {
                            System.out.print("Enter the amount you want to deposit:");
                            int amount = scan.nextInt();
                            customer.deposit(account, amount, null, user.getFullName());
                            valid = true;
                        } else {
                            System.out.println("Invalid Account ID!!!!");
                        }
                    } while (!valid);
                    scan.nextLine();
                    break;
                case "3":
                case "withdraw":
                    System.out.println("Choose the account you want to withdraw money to.");
                    customer.printAccount(accounts);

                    System.out.print("Enter the account ID: ");
                    int accountId2 = scan.nextInt();
                    System.out.print("Enter the amount you want to withdraw: ");
                    int amount2 = scan.nextInt();
                    Account account2 = ((Customer) user).getAccountById(accountId2);
                    if (account2 == null) {
                        System.out.println("Invalid Account ID!!!!");
                        scan.nextLine();
                        break;
                    }
                    customer.withdraw(account2, amount2, null);
                    scan.nextLine();
                    break;
                case "4":
                case "transfer":
                    System.out.println("Choose the account you want to transfer money from:");
                    customer.printAccount(accounts);
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
                            customer.transferMoney(transferAmount, srcAccountId, recipientAccId);
                            validId = true;
                        } else {
                            System.out.println("Invalid Account ID!!!!");
                        }
                    } while (!validId);
                    scan.nextLine();
                    break;
                case "5":
                case "statement":
                    System.out.println("Choose the account you want to withdraw money to.");
                    customer.printAccount(accounts);
                    System.out.print("Enter the account ID: ");
                    int accountId3 = scan.nextInt();
                    Account account = (customer.getAccountById(accountId3));
                    customer.getDetailedAccountStatment(account);
                    scan.nextLine();
                    break;
                case "6":
                case "filter":
                    System.out.println("-----Filter Transaction----");
                    System.out.println("1. Today\n" +
                            "2. Yesterday\n" +
                            "3. Last Week\n" +
                            "4. Last 7 Days\n" +
                            "5. Last Month\n" +
                            "6. Last 30 Days\n" +
                            "7. Specific Date\n" +
                            "8. Date & Time\n" +
                            "9. Back");
                    System.out.print("Enter your choice:");
                    String filterChoice = scan.nextLine();
                    customer.printTransactionDetails(customer.filterTransaction(filterChoice, scan));
                    break;
                case "7":
                case "change":
                case "card":
                    System.out.println("Choose the account you want to withdraw money to.");
                    customer.printAccount(accounts);
                    System.out.print("Enter the account ID: ");
                    int accountId4 = scan.nextInt();
                    scan.nextLine();
                    Account userAccount = (customer.getAccountById(accountId4));
                    userAccount.changeCardType(userAccount, scan);
                    break;
                case "8":
                case "logout":
                case "exit":
                    continueMenu = false;
                    return;
            }
        }

    }

    public static void bankerStartMenu(Scanner scan, User user, Bank bank) {
        boolean continueMenu = true;
        while (continueMenu) {
            System.out.println("\n====== Welcome " + user.getFullName() + "! ======");
            System.out.println("Choose the operation you want to do:");
            System.out.println("1- Add new customer");
            System.out.println("2- View Customer History");
            System.out.println("3- Exit");
            String userInput = scan.nextLine().toLowerCase();
            switch (userInput) {
                case "1":
                case "account":
                case "create":
                    addCustomerMenu(bank, scan, (Banker) user);
                    break;
                case "2":
                case "view":
                case "history":
                    System.out.println("Enter the customer id: ");
                    int customerId = scan.nextInt();
                    scan.nextLine();
                    Customer customer = bank.getCustomerByCustomerId(customerId);

                   for (Account acc: customer.getAccounts() ){
                       customer.getDetailedAccountStatment(acc);

                   }

                    break;
                case "3":
                case "logout":
                case "exit":
                    continueMenu = false;
                    return;
            }
        }

    }


    public static void createAccountMenu(Scanner scan, User user) {
        String type;
        do {
            System.out.println("==== Choose Account Type: ====");
            System.out.println("A- Checking");
            System.out.println("B- Saving");
            System.out.println("Enter your Choice :");
            String input = scan.nextLine().toLowerCase();
            type = Account.validateAccountType(input);
        } while (type == null);
        Account acc = Account.createAccount(type, user);

        System.out.println("New " + type + " Account Created Successfully! \n" +
                "Your Account Number: " + acc.getAccountId());
        System.out.println("Please create a password for this account.");
        String pass = scan.nextLine();
        acc.setPassword(SecurityUtil.hashPassword(pass));

    }


    public static void signUpMenu(Bank bank, Scanner scan) {
        // add input validation later
        System.out.print("Enter Username:");
        String customerUsername = scan.nextLine().trim();
        System.out.print("Enter your first name:");
        String firstName = scan.nextLine().trim();
        System.out.print("Enter your last name:");
        String lastName = scan.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scan.nextLine();
        System.out.print("""
                Choose Account Type\s
                A- Checking\s
                B- Saving\s
                """);
        String type = scan.nextLine().toLowerCase();
        Customer customer = Customer.createCustomer(firstName, lastName, customerUsername, password, type);
        bank.addUser(customer);
        System.out.println("Congrats, Your Banking Account Created Successfully!");
        System.out.println("Your username: " + customer.getUsername() + " & customer Id: " + customer.getCustomerId());
    }

    public static void addCustomerMenu(Bank bank, Scanner scan, Banker banker) {
        // add input validation later
        System.out.print("Enter Customer Username:");
        String customerUsername = scan.nextLine().trim();
        System.out.print("Enter Customer first name:");
        String firstName = scan.nextLine().trim();
        System.out.print("Enter Customer last name:");
        String lastName = scan.nextLine().trim();
        String pass = generateTemporaryPassword();
        System.out.print("""
                Choose Account Type\s
                A- Checking\s
                B- Saving\s
                """);
        String type = scan.nextLine().toLowerCase();
        Customer customer = Customer.createCustomer(firstName, lastName, customerUsername, pass, type);
        bank.addUser(customer);
        banker.saveBankerOperations(customer);
        System.out.println("Temporary Password: " + pass);
        System.out.println("Customer Id: " + customer.getCustomerId() +
                ", Account Id: " + (customer.getAccounts().stream().max(Comparator.comparing(Account::getCreatedAt)).orElse(null)).getAccountId());
    }

    private static String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static void loginMenu(Bank bank, Scanner scan) {
        boolean successLogin = false;
        do {
            System.out.print("Enter Username: ");
            String username = scan.nextLine();
            System.out.print("Enter password: ");
            String pass = scan.nextLine();
            System.out.println();
            User user = bank.login(username, pass, scan);
            if (user != null) {
                System.out.println("Logged in successfully");
                successLogin = true;
                if (user instanceof Customer) {
                    startMenu(scan, user, bank);
                } else if (user instanceof Banker){
                    bankerStartMenu(scan, user, bank);
                }
            } else {
                System.out.println("Invalid username or password, please try again!");
            }
        } while (!successLogin);


    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Bank bank = new Bank();
        boolean exitProgram = false;
        do {
            System.out.println("\u001B[36m====== ACME BANKING APP ======\u001B[0m");
            // System.out.println("====== Welcome to ACME Banking App! ======");
            System.out.println("Choose the operation you want to do:");
            System.out.println("1- Login");
            System.out.println("2- SignUp");
            System.out.println("3- Exit");
            String userInput = scan.nextLine().toLowerCase();

            switch (userInput) {
                case "1":
                case "login":
                    loginMenu(bank, scan);
                    break;
                case "2":
                case "sign up":
                    signUpMenu(bank, scan);
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


