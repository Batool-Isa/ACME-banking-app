package com.acme.banking;

import java.time.LocalDateTime;

public class Transaction {
private int transactionId;
private TransactionType  transactionType;
private LocalDateTime dateTime;
private String doneBy;
private double balance;
private double amount;
private int transferId;
int startId = 100000;
public enum TransactionType {
DEPOSIT,
WITHDRAW
}

    public Transaction(TransactionType  type, String doneBy, double balance, double amount, int transferId) {
       startId++;
        this.transactionId = startId;
        this.transactionType = type;
        this.dateTime = LocalDateTime.now();
        this.doneBy = doneBy;
        this.balance = balance;
        this.amount = amount;
        this.transferId = transferId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
