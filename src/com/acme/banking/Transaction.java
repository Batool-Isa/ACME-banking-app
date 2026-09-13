package com.acme.banking;

import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private TransactionType transactionType;
    private LocalDateTime dateTime;
    private String doneBy;
    private double balance;
    private double amount;
    private Integer transferId;
    private int accountId;
    int startId = 100000;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        OVERDRAFT_PENALTY
    }

    public Transaction(TransactionType type, String doneBy, double balance, double amount, int accountId, Integer transferId) {
        startId++;
        this.transactionId = startId;
        this.transactionType = type;
        this.dateTime = LocalDateTime.now();
        this.doneBy = doneBy;
        this.balance = balance;
        this.amount = amount;
        this.transferId = transferId;
        this.accountId = accountId;
    }

    public int getTransferId() {
        return transferId;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getAccountId() {
        return accountId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
}
