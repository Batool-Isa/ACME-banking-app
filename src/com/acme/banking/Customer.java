package com.acme.banking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Customer extends User implements IBankingOperations {
    private int customerId;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactionsList;
    private boolean firstLogin;
    private static int idStart = 5000;
    private static Integer transferId = 60000;

    public Customer(String firstName, String lastName, String username, String password, String role) {
        super(firstName, lastName, username, password, role);
        idStart++;
        this.customerId = idStart;
        this.accounts = new ArrayList<>();
        this.transactionsList = new ArrayList<>();
        this.firstLogin = false;
    }

    public int getCustomerId() {
        return customerId;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public static Customer createCustomer(String firstName, String lastName, String username, String password, String accType) {
        Customer customer = new Customer(firstName, lastName, username, password, "Customer");
        Account account = Account.createAccount(accType, customer);
        System.out.println("new account id: " + account.getAccountId());
        return customer;
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);

    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;

        if (customerId > idStart) {
            idStart = customerId;
        }
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

    public Account getAccountById(int accId) {
        return accounts.stream().filter(acc -> acc.getAccountId() == accId)
                .findFirst().orElse(null);
    }

    public void printAccount(ArrayList<Account> accounts) {
        for (Account acc : accounts) {
            System.out.println("Account ID: " + acc.getAccountId() +
                    " Account Type: " + acc.getType());
        }
    }

    public String checkDeposit(Account acc, String doneBy, Bank bank) {
        Customer cus = bank.getCustomerByAccountId(acc.getAccountId());
        if (cus != null) {
            if (cus.getFullName().equalsIgnoreCase(doneBy)) {
                return "Same";
            } else {
                return "Different";
            }
        }
return null;
    }

    public double getUserDepositTotal(Account acc) {
        ArrayList<Transaction> todayTransaction = filterTodayTransaction();
        return todayTransaction.stream()
                .filter(t -> t.getAccountId() == acc.getAccountId()
                        && t.getTransactionType() == Transaction.TransactionType.DEPOSIT)
                .mapToDouble(Transaction::getAmount).sum();

    }

    @Override
    public void deposit(Account acc, double amount, Integer transferId, String doneBy, Bank bank) {
        //System.out.println("user balance before deposit: " + acc.getBalance());
        if (amount <= 0) {
            System.out.println(ConsoleColors.RED + "Invalid Amount!!" + ConsoleColors.RESET);
            return;
        }

        String checkOwner = checkDeposit(acc, doneBy, bank);
        double limit = acc.getCard().getCardDepositLimit(checkOwner);
        double possibleDeposit = amount + getUserDepositTotal(acc);
        if (checkOwner.equalsIgnoreCase("Same")
                && (possibleDeposit > limit) && transferId == null) {
            System.out.println(ConsoleColors.RED +
                    "With this deposit daily deposit to your own account limit reached. Please try again tomorrow."
                    + ConsoleColors.RESET);
            return;

        }
        double newBalance = acc.getBalance() + amount;
        acc.setBalance(newBalance);
        Transaction trans = new Transaction(Transaction.TransactionType.DEPOSIT, doneBy, newBalance, amount, acc.getAccountId(), transferId);
        if (transferId != null) {
            trans.setTransferId(transferId);
        }
        addTransaction(trans);
        saveCustomerTransaction(trans);
        if (transferId == null) {
            System.out.println("Amount deposit successfully. Your Balance for Account " + acc.getAccountId() + " :" + acc.getBalance());
        }
        if (!acc.isActive() && newBalance >= 0) {
            acc.setActive(true);
            acc.setOverDraftCounter(0);
            //System.out.println("acc overdraft: "+acc.getOverDraftCounter());
            System.out.println("Your Account is Active Now!");
        }
    }

    public double getUserWithdrawTotal(Account acc) {
        ArrayList<Transaction> todayTransaction = filterTodayTransaction();
        return todayTransaction.stream()
                .filter(t -> t.getAccountId() == acc.getAccountId() && t.getTransactionType() == Transaction.TransactionType.WITHDRAW)
                .mapToDouble(Transaction::getAmount).sum();
    }

    @Override
    public void withdraw(Account acc, double amount, Integer transferId, Bank bank) {
        //System.out.println("user balance before Withdraw: " + acc.getBalance());
        double limit = acc.getCard().getCardWithdrawLimit();
        double total = getUserWithdrawTotal(acc);
        double possibleWithdraw = amount + total;
        Customer cus = bank.getCustomerByAccountId(acc.getAccountId());
        if (amount <= 0) {
            System.out.println(ConsoleColors.RED + "Invalid Amount!!" + ConsoleColors.RESET);
            return;
        }
        if (!acc.isActive()) {
            System.out.println(ConsoleColors.RED +
                    "Your Account has been deactivated after reaching the overdraft limit. " +
                    "Please deposit money to resolve negative balance" + ConsoleColors.RESET);
            return;
        }
        if (possibleWithdraw > limit && transferId == null) {
            System.out.println(ConsoleColors.RED +
                    "Daily withdrawal limit reached. You have already withdrawn " +
                    total + " BHD today, and your limit is " + limit +
                    " BHD. Please try again tomorrow." +
                    ConsoleColors.RESET);
            return;
        }
        double oldBalance = acc.getBalance();
        if (oldBalance < 0) {
            if (amount > 100) {
                System.out.println(ConsoleColors.RED + "Transaction declined: " +
                        " You cannot withdraw more than 100 BHD while your account has a negative balance."
                        + ConsoleColors.RESET);
            } else {
                acc.setOverDraftCounter(acc.getOverDraftCounter() + 1);
                double balance = oldBalance - (amount);
                acc.setBalance(balance);
                // save transaction logic here
                Transaction trans = new Transaction(Transaction.TransactionType.WITHDRAW, cus.getFullName(), balance, amount, acc.getAccountId(), transferId);
                if (transferId != null) {
                    trans.setTransferId(transferId);
                }
                addTransaction(trans);
                saveCustomerTransaction(trans);
                getOverDraftPenalty(acc);
            }
        } else {

            double balance = acc.getBalance() - amount;
            acc.setBalance(balance);
            // save transaction logic here
            assert cus != null;
            Transaction trans = new Transaction(Transaction.TransactionType.WITHDRAW, cus.getFullName(), balance, amount, acc.getAccountId(), transferId);
            if (transferId != null) {
                trans.setTransferId(transferId);
                System.out.println(ConsoleColors.GREEN + "Amount transferred successfully!" + ConsoleColors.RESET);
                System.out.println("Your Balance for Account " +
                        acc.getAccountId() + " :" + acc.getBalance() + " BHD");
            } else {
                System.out.println(ConsoleColors.GREEN
                        + "Amount withdraw successfully." + ConsoleColors.RESET);
                System.out.println("Your Balance for Account " +
                        acc.getAccountId() + " :" + acc.getBalance() + " BHD");
            }
            addTransaction(trans);
            saveCustomerTransaction(trans);
            if (balance < 0) {
                acc.setOverDraftCounter(acc.getOverDraftCounter() + 1);
                getOverDraftPenalty(acc);
            }
        }
    }


    public void getOverDraftPenalty(Account acc) {
        double balance = acc.getBalance() - 35;
        acc.setBalance(balance);
        Transaction trans2 = new Transaction(Transaction.TransactionType.OVERDRAFT_PENALTY, this.getFullName(), balance, 35, acc.getAccountId(), null);
        addTransaction(trans2);
        saveCustomerTransaction(trans2);
        System.out.println(
                ConsoleColors.YELLOW +
                        "Overdraft fee of 35 BHD has been applied." +
                        ConsoleColors.RESET
        );
    }

    public double getUserTransferTotal(Account acc) {
        ArrayList<Transaction> todayTransaction = filterTodayTransaction();
        return todayTransaction.stream()
                .filter(t -> t.getAccountId() == acc.getAccountId()
                        && t.getTransactionType() == Transaction.TransactionType.WITHDRAW
                        && t.getTransferId() != null
                        && !t.getDoneBy().equals(this.getFullName()))
                .mapToDouble(Transaction::getAmount).sum();

    }

    public double getUserTransferTotalToOwnAccount(Account acc) {
        ArrayList<Transaction> todayTransaction = filterTodayTransaction();
        return todayTransaction.stream()
                .filter(t -> t.getAccountId() == acc.getAccountId()
                        && t.getTransactionType() == Transaction.TransactionType.WITHDRAW
                        && t.getTransferId() != null
                        && t.getDoneBy().equals(this.getFullName()))
                .mapToDouble(Transaction::getAmount).sum();

    }

    @Override
    public void transferMoney(double amount, int srcAccount, int destinationAccount, Bank bank) {
        Customer srcCustomer = bank.getCustomerByAccountId(srcAccount);
        Customer customer = bank.getCustomerByAccountId(destinationAccount);

        int id = transferId;
        transferId++;
        if (srcCustomer == customer) {
            Account src = getAccountById(srcAccount);
            double limit = src.getCard().getCardTransferLimitToOwn();
            double total = getUserTransferTotalToOwnAccount(src);
            double possibleTransfer = amount + total;
            if (possibleTransfer > limit && transferId != null) {
                System.out.println(ConsoleColors.RED +
                        "Daily transfer limit reached. You have already transferred to your account " +
                        total + " BHD today, and your limit is " + limit +
                        " BHD. Please try again tomorrow." +
                        ConsoleColors.RESET);
               return;
            }
            srcCustomer.withdraw(srcCustomer.getAccountById(srcAccount), amount, id, bank);
            Account destAccount = customer.getAccountById(destinationAccount);
            customer.deposit(destAccount, amount, id, srcCustomer.getFullName(), bank);
        } else {
            Account src = getAccountById(srcAccount);
            double limit = src.getCard().getCardTransferLimit();
            double totalToDiff = getUserTransferTotal(src);
            double possibleTransferToDiff = amount + totalToDiff;
            if (possibleTransferToDiff > limit) {
                System.out.println(ConsoleColors.RED +
                        "Daily transfer limit reached. You have already transferred to different account " +
                        totalToDiff + " BHD today, and your limit is " + limit +
                        " BHD. Please try again tomorrow." +
                        ConsoleColors.RESET);

                return;
            }
            srcCustomer.withdraw(srcCustomer.getAccountById(srcAccount), amount, id, bank);
            Account destAccount = customer.getAccountById(destinationAccount);
            customer.deposit(destAccount, amount, id, srcCustomer.getFullName(), bank);
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
            writer.write(String.format(
                    "Balance After Transaction: %.2f",
                    trans.getBalance()
            ));
            writer.newLine();

            writer.write("Done By: " + trans.getDoneBy());
            writer.newLine();
            if (trans.getTransferId() != null) {
                writer.write("Transfer Id: " + trans.getTransferId());
                writer.newLine();
            }
            writer.write("----------------------------------------");
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save customer transaction :" + getCustomerId(), e);
        }
    }


    public void getDetailedAccountStatment(Account acc) {
        System.out.println();
        System.out.println("Account Information");
        System.out.println("---------------------------------------");
        System.out.println("Account ID     :" + acc.getAccountId());
        System.out.println("Account Type   :" + acc.getType());
        System.out.println("Balance        :" + acc.getBalance());
        System.out.println("Account Status :" + (acc.isActive() ? "Active" : "Inactive"));
        System.out.println();
        System.out.println("Card Information");
        System.out.println("---------------------------------------");
        System.out.println("Card Type     :" + acc.getCard().getType());
        System.out.println("Card Number   :" + acc.getCard().getCardNumber());
        System.out.println("---------------------------------------");
        System.out.println();
        System.out.println("TRANSACTION HISTORY");
        System.out.println("---------------------------------------");
        ArrayList<Transaction> transactionsFiltered = transactionsList.stream()
                .filter(t -> t.getAccountId() == acc.getAccountId())
                .collect(Collectors.toCollection(ArrayList::new));
        if (transactionsFiltered.isEmpty()) {
            System.out.println("No Transaction Found!");
        } else {
            printTransactionDetails(transactionsFiltered);
        }

    }

    public void printTransactionDetails(ArrayList<Transaction> list) {
        for (Transaction t : list) {
            System.out.println("Transaction ID: " + t.getTransactionId());
            System.out.println("Type: " + t.getTransactionType());
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println("Date: " + t.getDateTime().format(formatter));
            System.out.println("Amount: " + t.getAmount());
            System.out.println("Balance After Transaction: " + t.getBalance());
            System.out.println("Done By: " + t.getDoneBy());
            System.out.println("---------------------------------------");
        }
    }

    public ArrayList<Transaction> filterTodayTransaction() {
        LocalDate today = LocalDate.now();

        return this.getTransactionsList().stream()
                .filter(t -> t.getDateTime().toLocalDate().equals(today))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Transaction> filterTransaction(String filterChoice, Scanner scan) {
        LocalDate today = LocalDate.now();
        switch (filterChoice) {
            case "1":
             return  filterTodayTransaction();
            case "2":
                LocalDate yesterday = today.minusDays(1);
                return new ArrayList<Transaction>(this.getTransactionsList()
                        .stream().filter(t -> t.getDateTime().toLocalDate().equals(yesterday))
                        .toList());
            case "3":
                LocalDate startOfWeek = today.with(DayOfWeek.SUNDAY); // each week will start on sunday -- saturday
                LocalDate startOfLastWeek = startOfWeek.minusWeeks(1);
                LocalDate endOfLastWeek = startOfWeek.minusDays(1);
                return new ArrayList<Transaction>(this.getTransactionsList()
                        .stream().filter(t -> {
                            LocalDate date = t.getDateTime().toLocalDate();
                            return !date.isBefore(startOfLastWeek) && !date.isAfter(endOfLastWeek);
                        })
                        .toList());
            case "4": //last 7 days
                LocalDate lastweek = today.minusDays(6);
                return new ArrayList<Transaction>(this.getTransactionsList()
                        .stream().filter(t -> {
                            LocalDate date = t.getDateTime().toLocalDate();
                            return !date.isBefore(lastweek) && !date.isAfter(today);
                        })
                        .toList());
            case "5":
                LocalDate firstDayOfMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate lastDayOfMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

                return new ArrayList<Transaction>(this.getTransactionsList()
                        .stream().filter(t -> {
                            LocalDate date = t.getDateTime().toLocalDate();
                            return !date.isBefore(firstDayOfMonth) && !date.isAfter(lastDayOfMonth);
                        })
                        .toList());
            case "6":
                LocalDate last30days = today.minusDays(29);
                return new ArrayList<Transaction>(this.getTransactionsList()
                        .stream().filter(t -> {
                            LocalDate date = t.getDateTime().toLocalDate();
                            return !date.isBefore(last30days) && !date.isAfter(today);
                        })
                        .toList());
            case "7":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate date = null;
                while (date == null) {
                    System.out.print("Enter date (YYYY-MM-DD), e.g., 2026-09-13: ");
                    String input = scan.nextLine();
                    try {
                        date = LocalDate.parse(input, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println(ConsoleColors.RED +
                                "Invalid format! Please try again using YYYY-MM-DD." + ConsoleColors.RESET);
                    }
                }
                LocalDate choosenDate = date;
                return new ArrayList<Transaction>(this.getTransactionsList()
                        .stream().filter(t -> t.getDateTime().toLocalDate().equals(choosenDate))
                        .toList());

            case "8":
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");

                LocalDate date1 = null;
                LocalTime time = null;

                while (date1 == null || time == null) {
                    System.out.print("Enter date (YYYY-MM-DD), e.g., 2026-09-13: ");
                    String input = scan.nextLine();
                    System.out.print("Enter Time (HH:mm), e.g., 10:40: ");
                    String inputTime = scan.nextLine();
                    try {
                        date1 = LocalDate.parse(input, formatter1);
                        time = LocalTime.parse(inputTime, formatter2);
                    } catch (DateTimeParseException e) {
                        date1 = null;
                        time = null;
                        System.out.println(ConsoleColors.RED +
                                "Invalid format! Please try again." + ConsoleColors.RESET);
                    }
                }
                LocalDate choosenDate1 = date1;
                LocalTime choosenTime = time;
                return new ArrayList<Transaction>(this.getTransactionsList()
                        .stream().filter(t -> t.getDateTime().toLocalDate().equals(choosenDate1) && t.getDateTime().toLocalTime().equals(choosenTime))
                        .toList());
        }

        return null;
    }
}



