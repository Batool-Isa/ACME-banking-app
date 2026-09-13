package com.acme.banking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Customer extends User {
    private int customerId;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactionsList = new ArrayList<>();
    private static int idStart = 5000;
    private static int transferId = 60000;

    public Customer(String firstName, String lastName, String username, String password, String role) {
        super(firstName, lastName, username, password, role);
        idStart++;
        this.customerId = idStart;
        this.accounts = new ArrayList<>();
        this.transactionsList = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public static Customer createCustomer(String firstName, String lastName, String username, String password, String accType) {
        Customer customer = new Customer(firstName, lastName, username, password, "Customer");
        Account account = Account.createAccount(accType, customer);
        account.setFirstLogin(true);
        System.out.println("new account id: " + account.getAccountId());
        return customer;
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);

    }

    public ArrayList<Transaction> getTransactionsList() {
        return transactionsList;
    }


    public void addTransaction(Transaction trans) {

        this.transactionsList.add(trans);
    }


    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getAccountById(int accId) {
        return accounts.stream().filter(acc -> acc.getAccountId() == accId).findFirst().orElse(null);
    }

    @Override
    public double deposit(Account acc, double amount, Integer transferId) {
        System.out.println("user balance before seposit: " + acc.getBalance());

        if (amount < 0) {
            System.out.println("Invalid Amount!!");
            return -1;
        }
        double newBalance = acc.getBalance() + amount;
        acc.setBalance(newBalance);
        Transaction trans = new Transaction(Transaction.TransactionType.DEPOSIT, this.getFullName(), newBalance, amount, acc.getAccountId(), null);
        if (transferId != null) {
            trans.setTransferId(transferId);
        }
        addTransaction(trans);
        saveCustomerTransaction(trans);
        System.out.println("Amount deposit successfully. Your Balance for Account " + acc.getAccountId() + " :" + acc.getBalance());
        if(!acc.isActive() && newBalance>=0){
            acc.setActive(true);
            acc.setOverDraftCounter(0);
            //System.out.println("acc overdraft: "+acc.getOverDraftCounter());
            System.out.println("Your Account is Active Now!");
        }
        return newBalance;

    }

    @Override
    public double withdraw(Account acc, double amount, Integer transferId) {
        System.out.println("user balance before Withdraw: " + acc.getBalance());

        if (amount < 0) {
            System.out.println("Invalid Amount!!");
            return -1;
        }
        if (!acc.isActive()) {
            System.out.println("over draft num" + acc.getOverDraftCounter());
            System.out.println("Your Account is not active , please deposit moeny to resolve negative balance");
            return -1;
        }
        double oldBalance = acc.getBalance();
        if (oldBalance < 0) {
            if (amount > 100) {
                System.out.println("This Transaction can't be done. You can't withdraw more than 100$ if account is negative.");
                return -1;
            } else {
                acc.setOverDraftCounter(acc.getOverDraftCounter() + 1);
                double balance = oldBalance - (amount);
                acc.setBalance(balance);
                // save transaction logic here
                Transaction trans = new Transaction(Transaction.TransactionType.WITHDRAW, this.getFullName(), balance, amount, acc.getAccountId(), null);
                if (transferId != null) {
                    trans.setTransferId(transferId);
                }
                addTransaction(trans);
                saveCustomerTransaction(trans);
                getOverDraftPenalty(acc);
                return balance;
            }
        } else {
            double balance = acc.getBalance() - amount;
            acc.setBalance(balance);
            // save transaction logic here
            Transaction trans = new Transaction(Transaction.TransactionType.WITHDRAW, this.getFullName(), balance, amount, acc.getAccountId(), null);
            if (transferId != null) {
                trans.setTransferId(transferId);
            }
            addTransaction(trans);
            saveCustomerTransaction(trans);
            System.out.println("Amount withdraw successfully. Your Balance for Account " + acc.getAccountId() + " :" + acc.getBalance());
            if (balance < 0) {
                acc.setOverDraftCounter(acc.getOverDraftCounter() + 1);
                getOverDraftPenalty(acc);
            }
            return balance;
        }
    }

    public void getOverDraftPenalty(Account acc) {
        double balance = acc.getBalance() - 35;
        acc.setBalance(balance);
        Transaction trans2 = new Transaction(Transaction.TransactionType.OVERDRAFT_PENALTY, this.getFullName(), balance, 35, acc.getAccountId(), null);
        addTransaction(trans2);
        saveCustomerTransaction(trans2);
    }

    @Override
    public void transferMoney(double amount, int srcAccount, int destinationAccount) {
        Customer srcCustomer = Bank.getCustomerByAccountId(srcAccount);
        int id = transferId++;
        if (srcCustomer != null) {
            srcCustomer.withdraw(srcCustomer.getAccountById(srcAccount), amount, id);
            Customer customer = Bank.getCustomerByAccountId(destinationAccount);
            Account destAccount = customer.getAccountById(destinationAccount);
            customer.deposit(destAccount, amount, id);
        }
    }

    public void saveCustomerTransaction(Transaction trans) {

        File folder = new File("data/customerFiles");

        if (!folder.exists()) {
            folder.mkdir();
        }

        File customerFile = new File(
                folder,
                "Customer-" + this.getFullName() + "-" + this.getCustomerId()
        );

        try {

            if (!customerFile.exists()) {
                customerFile.createNewFile();

                System.out.println(
                        "Customer file created: "
                                + customerFile.getAbsolutePath()
                );
            }

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(customerFile, true));

            writer.write("Account ID: " + trans.getAccountId());
            writer.newLine();
            writer.write("Transaction ID: " + trans.getTransactionId());
            writer.newLine();

            writer.write("Type: " + trans.getTransactionType());
            writer.newLine();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String formattedTx = trans.getDateTime().format(formatter);

            writer.write("Date: " + formattedTx);
            writer.newLine();

            writer.write("Amount: " + trans.getAmount());
            writer.newLine();

            writer.write(String.format("Balance After Transaction: %.2f%n", trans.getBalance()));
            writer.newLine();

            writer.write("Done By: " + trans.getDoneBy());
            writer.newLine();

            writer.write("----------------------------------------");
            writer.newLine();

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



