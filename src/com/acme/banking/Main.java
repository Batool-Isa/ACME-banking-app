package com.acme.banking;

import java.util.*;
import java.util.Scanner;

public class Main {
    public static void startMenu(Scanner scan, User user, Bank bank) {
        boolean continueMenu = true;
        while (continueMenu) {
            printCustomerMenu(user);
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
                    depositMenu(scan, customer, accounts, bank, user);
                    break;
                case "3":
                case "withdraw":
                    withdrawMenu(scan, customer, accounts, bank, user);
                    break;
                case "4":
                case "transfer":
                    transferMenu(scan, customer, accounts, bank);
                    break;
                case "5":
                case "statement":
                    statementMenu(scan, customer, accounts);
                    break;
                case "6":
                case "filter":
                    filterMenu(scan, customer);
                    break;
                case "7":
                case "manage":
                case "card":
                    manageCardMenu(scan, customer, accounts);
                    break;
                case "8":
                case "logout":
                case "exit":
                    System.out.println();
                    System.out.println(ConsoleColors.BOLD + ConsoleColors.GREEN +
                            "Thank you for using ACME Bank App!" +
                            ConsoleColors.RESET);
                    System.out.println();
                    continueMenu = false;
                    return;
            }
        }
    }

    public static void printCustomerMenu(User user) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========================================" +
                ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "        Welcome " + user.getFullName() + "!" +
                ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========================================" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.BOLD +
                "Please select an operation:" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.println(" 1 - Create New Banking Account");
        System.out.println(" 2 - Deposit Money");
        System.out.println(" 3 - Withdraw Money");
        System.out.println(" 4 - Transfer Money");
        System.out.println(" 5 - Account Statement");
        System.out.println(" 6 - Filter Transactions");
        System.out.println(" 7 - Manage Card Type");
        System.out.println(" 8 - Logout");
        System.out.println();
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                "Enter your choice: " +
                ConsoleColors.RESET);
    }

    public static void depositMenu(Scanner scan, Customer customer, ArrayList<Account> accounts, Bank bank, User user) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== DEPOSIT MONEY ==========" +
                ConsoleColors.RESET);
        System.out.println();
        customer.printAccount(accounts);
        boolean valid = false;
        do {
            System.out.println();
            System.out.print(ConsoleColors.YELLOW +
                    "Enter the account ID: " +
                    ConsoleColors.RESET);
            int accountId = scan.nextInt();
            Account account = bank.getAccount(accountId);
            if (account != null) {
                System.out.print(ConsoleColors.YELLOW +
                        "Enter the amount you want to deposit: " +
                        ConsoleColors.RESET);
                int amount = scan.nextInt();
                customer.deposit(
                        account,
                        amount,
                        null,
                        user.getFullName(),
                        bank
                );
                valid = true;
            } else {
                System.out.println(ConsoleColors.RED +
                        "Invalid Account ID. Please try again." +
                        ConsoleColors.RESET);
            }
        } while (!valid);
        scan.nextLine();
    }

    public static void withdrawMenu(Scanner scan, Customer customer, ArrayList<Account> accounts, Bank bank, User user) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== WITHDRAW MONEY ==========" +
                ConsoleColors.RESET);
        System.out.println();
        customer.printAccount(accounts);
        System.out.println();
        System.out.print(ConsoleColors.YELLOW +
                "Enter the account ID: " +
                ConsoleColors.RESET);
        int accountId2 = scan.nextInt();
        Account account2 = ((Customer) user).getAccountById(accountId2);
        if (account2 == null) {
            System.out.println();
            System.out.println(ConsoleColors.RED +
                    "Invalid Account ID. Please try again." +
                    ConsoleColors.RESET);
            scan.nextLine();
            return;
        }
        System.out.print(ConsoleColors.YELLOW +
                "Enter the amount you want to withdraw: " +
                ConsoleColors.RESET);
        int amount2 = scan.nextInt();

        customer.withdraw(account2, amount2, null, bank);
        scan.nextLine();
    }

    public static void transferMenu(Scanner scan, Customer customer, ArrayList<Account> accounts, Bank bank) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== TRANSFER MONEY ==========" +
                ConsoleColors.RESET);
        System.out.println();
        customer.printAccount(accounts);
        boolean validId = false;
        do {
            System.out.println();
            System.out.print(ConsoleColors.YELLOW +
                    "Enter the account ID: " +
                    ConsoleColors.RESET);
            int srcAccountId = scan.nextInt();
            Account srcAccount =
                    (customer.getAccountById(srcAccountId));
            if (srcAccount != null) {
                System.out.print(ConsoleColors.YELLOW +
                        "Enter the recipient account ID: " +
                        ConsoleColors.RESET);
                int recipientAccId = scan.nextInt();
                Customer recipientCustomer =
                        bank.getCustomerByAccountId(recipientAccId);
                if (recipientCustomer == null) {
                    System.out.println(ConsoleColors.RED +
                            "Invalid recipient account ID. Please try again." +
                            ConsoleColors.RESET);
                    continue;
                }
                System.out.print(ConsoleColors.YELLOW +
                        "Enter the amount you want to transfer: " +
                        ConsoleColors.RESET);
                int transferAmount = scan.nextInt();
                customer.transferMoney(
                        transferAmount,
                        srcAccountId,
                        recipientAccId,
                        bank
                );
                validId = true;
            } else {
                System.out.println(ConsoleColors.RED +
                        "Invalid Account ID. Please try again." +
                        ConsoleColors.RESET);
            }
        } while (!validId);
        scan.nextLine();
    }

    public static void statementMenu(Scanner scan, Customer customer, ArrayList<Account> accounts) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== ACCOUNT STATEMENT ==========" +
                ConsoleColors.RESET);
        System.out.println();
        customer.printAccount(accounts);
        System.out.println();
        System.out.print(ConsoleColors.YELLOW +
                "Enter the account ID: " +
                ConsoleColors.RESET);
        int accountId3 = scan.nextInt();
        Account account =
                (customer.getAccountById(accountId3));
        customer.getDetailedAccountStatment(account);
        scan.nextLine();
    }

    public static void filterMenu(Scanner scan, Customer customer) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== FILTER TRANSACTIONS ==========" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.println(
                ConsoleColors.YELLOW +
                        """
                                1. Today
                                2. Yesterday
                                3. Last Week
                                4. Last 7 Days
                                5. Last Month
                                6. Last 30 Days
                                7. Specific Date
                                8. Date & Time
                                9. Back
                                """ +
                        ConsoleColors.RESET
        );
        System.out.print(ConsoleColors.BOLD +
                "Enter your choice: " +
                ConsoleColors.RESET);
        String filterChoice = scan.nextLine();
        ArrayList<Transaction> filteredTransactions = customer.filterTransaction(filterChoice, scan);
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions associated with this filter.");
        } else {
            customer.printTransactionDetails(filteredTransactions);
        }
    }

    public static void changeCardMenu(Scanner scan, Customer customer, ArrayList<Account> accounts) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== CHANGE CARD TYPE ==========" +
                ConsoleColors.RESET);
        System.out.println();
        customer.printAccount(accounts);
        System.out.println();
        System.out.print(ConsoleColors.YELLOW +
                "Enter the account ID: " +
                ConsoleColors.RESET);
        int accountId4 = scan.nextInt();
        scan.nextLine();
        Account userAccount =
                (customer.getAccountById(accountId4));
        if (userAccount == null) {
            System.out.println(ConsoleColors.RED +
                    "Account not found." +
                    ConsoleColors.RESET);
            return;
        }
        userAccount.changeCardType(userAccount, scan);
    }

    public static void manageCardMenu(Scanner scan, Customer customer,
                                      ArrayList<Account> accounts) {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                    "========== MANAGE CARD ==========" +
                    ConsoleColors.RESET);
            System.out.println();
            System.out.println("1. View Card Details");
            System.out.println("2. Change Card Type");
            System.out.println("3. Back");
            System.out.println();

            System.out.print(ConsoleColors.YELLOW +
                    "Enter your choice: " +
                    ConsoleColors.RESET);

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    Account.viewCardDetails(scan, customer, accounts);
                    break;
                case 2:
                    changeCardMenu(scan, customer, accounts);
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println(ConsoleColors.RED +
                            "Invalid choice. Please try again." +
                            ConsoleColors.RESET);
            }
        }
    }

    public static void bankerStartMenu(Scanner scan, User user, Bank bank) {
        boolean continueMenu = true;
        while (continueMenu) {
            printBankerMenu(user);
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
                    customerHistoryMenu(scan, bank);
                    break;
                case "3":
                case "logout":
                case "exit":
                    System.out.println();
                    System.out.println(ConsoleColors.BOLD + ConsoleColors.GREEN +
                            "Thank you for using ACME Bank!" +
                            ConsoleColors.RESET);
                    System.out.println();
                    continueMenu = false;
                    return;
            }
        }
    }

    public static void printBankerMenu(User user) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========================================" +
                ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "        Welcome " + user.getFullName() + "!" +
                ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========================================" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.BOLD +
                "Please select an operation:" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.println(" 1 - Add New Customer");
        System.out.println(" 2 - View Customer History");
        System.out.println(" 3 - Logout");
        System.out.println();
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                "Enter your choice: " +
                ConsoleColors.RESET);
    }

    public static void customerHistoryMenu(Scanner scan, Bank bank) {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== CUSTOMER HISTORY ==========" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.print(ConsoleColors.YELLOW +
                "Enter the customer ID: " +
                ConsoleColors.RESET);
        int customerId = scan.nextInt();
        scan.nextLine();
        Customer customer = bank.getCustomerByCustomerId(customerId);
        if(customer == null){
            System.out.println("No customer have this customer id");
            return;
        }
        for (Account acc : customer.getAccounts()) {
            customer.getDetailedAccountStatment(acc);
        }
    }

    public static void createAccountMenu(Scanner scan, User user) {
        String type;
        do {
            System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN + "========== CHOOSE ACCOUNT TYPE ==========" + ConsoleColors.RESET);
            System.out.println("A - Checking");
            System.out.println("B - Saving");
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter your choice: " + ConsoleColors.RESET);
            String input = scan.nextLine().toLowerCase();
            type = Account.validateAccountType(input);
        } while (type == null);
        Account acc = Account.createAccount(type, user);
        if (acc == null) {
            return;
        }
        System.out.println(ConsoleColors.BOLD + ConsoleColors.GREEN + "New " + type + " Account Created Successfully!" + ConsoleColors.RESET);
        System.out.println("Your Account Number: " + acc.getAccountId());
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Please create a password for this account: " + ConsoleColors.RESET);
        String pass = scan.nextLine();
        acc.setPassword(SecurityUtil.hashPassword(pass));
    }

    public static void signUpMenu(Bank bank, Scanner scan) {
        // add input validation later
        String customerUsername;
        boolean exist = true;
        do {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter Username: " + ConsoleColors.RESET);
            customerUsername = scan.nextLine().trim();
            if (bank.checkUsername(customerUsername)) {
                System.out.println(ConsoleColors.RED + "Username already in use. Please choose another username." + ConsoleColors.RESET);
            } else {
                exist = false;
            }
        } while (exist);
        String firstName;
        boolean validFirstName = false;
        do {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter your first name: " + ConsoleColors.RESET);
            firstName = scan.nextLine().trim();
            if (firstName.isEmpty()) {
                System.out.println(ConsoleColors.RED + "First name cannot be empty." + ConsoleColors.RESET);
            } else if (!firstName.matches("[a-zA-Z ]+")) {
                System.out.println(ConsoleColors.RED + "First name should only contain letters." + ConsoleColors.RESET);
            } else if (firstName.length() < 3) {
                System.out.println(ConsoleColors.RED + "First name must contain at least 3 letters." + ConsoleColors.RESET);
            } else {
                validFirstName = true;
            }
        } while (!validFirstName);
        String lastName;
        boolean validlastName = false;
        do {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter your last name: " + ConsoleColors.RESET);
            lastName = scan.nextLine().trim();
            if (lastName.isEmpty()) {
                System.out.println(ConsoleColors.RED + "Last name cannot be empty." + ConsoleColors.RESET);
            } else if (!lastName.matches("[a-zA-Z ]+")) {
                System.out.println(ConsoleColors.RED + "Last name should only contain letters." + ConsoleColors.RESET);
            } else if (lastName.length() < 3) {
                System.out.println(ConsoleColors.RED + "Last name must contain at least 3 letters." + ConsoleColors.RESET);
            } else {
                validlastName = true;
            }
        } while (!validlastName);
        String password;
        boolean validPassword = false;
        do {
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter password: " + ConsoleColors.RESET);
            password = scan.nextLine();
            if (password.isBlank()) {
                System.out.println(ConsoleColors.RED + "Password cannot be empty." + ConsoleColors.RESET);
            } else if (password.length() < 8) {
                System.out.println(ConsoleColors.RED + "Password must be at least 8 characters." + ConsoleColors.RESET);
            } else {
                validPassword = true;
            }
        } while (!validPassword);
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN + "========== CHOOSE ACCOUNT TYPE ==========" + ConsoleColors.RESET);
        System.out.println("A - Checking");
        System.out.println("B - Saving");
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW + "Enter your choice: " + ConsoleColors.RESET);
        String type = scan.nextLine().toLowerCase();
        Customer customer = Customer.createCustomer(firstName, lastName, customerUsername, password, type);
        bank.addUser(customer);
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.GREEN + "Account created successfully!" + ConsoleColors.RESET);
        System.out.println("Username: " + customer.getUsername());
        System.out.println("Customer ID: " + customer.getCustomerId());
    }

    private static String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static void addCustomerMenu(Bank bank, Scanner scan, Banker banker) {
        // add input validation later
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== ADD NEW CUSTOMER ==========" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                "Enter Customer Username: " +
                ConsoleColors.RESET);
        String customerUsername = scan.nextLine().trim();
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                "Enter Customer First Name: " +
                ConsoleColors.RESET);
        String firstName = scan.nextLine().trim();
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                "Enter Customer Last Name: " +
                ConsoleColors.RESET);
        String lastName = scan.nextLine().trim();
        String pass = generateTemporaryPassword();
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========== CHOOSE ACCOUNT TYPE ==========" +
                ConsoleColors.RESET);
        System.out.println("A - Checking");
        System.out.println("B - Saving");
        System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                "Enter your choice: " +
                ConsoleColors.RESET);
        String type = scan.nextLine().toLowerCase();
        Customer customer = Customer.createCustomer(firstName, lastName, customerUsername, pass, type);
        customer.setFirstLogin(true);
        bank.addUser(customer);
        banker.saveBankerOperations(customer);
        String accountPassword = generateTemporaryPassword();
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.GREEN +
                "Customer account created successfully!" +
                ConsoleColors.RESET);
        System.out.println("Customer Temporary Password: " + pass);
        System.out.println("Account Temporary Password: " + accountPassword);

        System.out.println("Customer ID: " + customer.getCustomerId());
        Optional<Account> account = customer.getAccounts().stream()
                .max(Comparator.comparing(Account::getCreatedAt));
        account.ifPresent(a -> a.setPassword(accountPassword));
        account.ifPresent(acc -> System.out.println("Account ID: " + acc.getAccountId()));
    }

    public static void loginMenu(Bank bank, Scanner scan) {
        boolean successLogin = false;
        do {
            System.out.println();
            System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                    "========== LOGIN ==========" +
                    ConsoleColors.RESET);
            System.out.println();
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                    "Enter Username: " +
                    ConsoleColors.RESET);
            String username = scan.nextLine();
            System.out.print(ConsoleColors.BOLD + ConsoleColors.YELLOW +
                    "Enter Password: " +
                    ConsoleColors.RESET);
            String pass = scan.nextLine();
            System.out.println();
            User user = bank.login(username, pass, scan);
            if (user != null) {
                // System.out.println("Logged in successfully");
                successLogin = true;
                if (user instanceof Customer) {
                    startMenu(scan, user, bank);
                } else if (user instanceof Banker) {
                    bankerStartMenu(scan, user, bank);
                }
            }
        } while (!successLogin);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Bank bank = new Bank();
        boolean exitProgram = false;
        do {
            printMainMenu();
            String userInput = scan.nextLine();
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

    public static void printMainMenu() {
        System.out.println();
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========================================" +
                ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "           ACME BANKING APP" +
                ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD + ConsoleColors.CYAN +
                "========================================" +
                ConsoleColors.RESET);
        System.out.println();
        System.out.println("Please choose an option:");
        System.out.println();
        System.out.println("  1. Login");
        System.out.println("  2. Sign Up");
        System.out.println("  3. Exit");
        System.out.println();
        System.out.print("Enter your choice: ");
    }
}