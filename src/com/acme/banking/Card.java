package com.acme.banking;

import java.time.LocalDate;

public class Card {
    private int cardId;
    private CardType type;
    private int cardNumber;
    private LocalDate dateIssued;
    private LocalDate expiryDate;
    private int cvs;
    private int accountId;
    int idStart = 100;

    public enum CardType {
        MASTERCARD,
        MASTERCARD_TITANIUM,
        MASTERCARD_PLATINUM
    }

    public Card(CardType type, int cardNumber, int cvs) {
        idStart++;
        this.cardId = idStart;
        this.type = type;
        this.cardNumber = cardNumber;
        this.dateIssued = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(5);
        this.cvs = cvs;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getCvs() {
        return cvs;
    }

    public void setCvs(int cvs) {
        this.cvs = cvs;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getCardWithdrawLimit() {
        switch (type) {
            case MASTERCARD -> {
                return 5000;
            }
            case MASTERCARD_PLATINUM -> {
                return 10000;
            }
            case MASTERCARD_TITANIUM -> {
                return 20000;
            }
        }
        return 0;
    }

    public double getCardDepositLimit(String accountOwner) {
        switch (accountOwner) {
            case "Same":
                return 200000;
            case "Different":
                return 100000;
        }
        return 0;
    }

    public double getCardTransferLimit() {
        switch (type) {
            case MASTERCARD -> {
                return 10000;
            }
            case MASTERCARD_PLATINUM -> {
                return 20000;
            }
            case MASTERCARD_TITANIUM -> {
                return 40000;
            }
        }
        return 0;
    }
    public double getCardTransferLimitToOwn() {
        switch (type) {
            case MASTERCARD -> {
                return 20000;
            }
            case MASTERCARD_PLATINUM -> {
                return 40000;
            }
            case MASTERCARD_TITANIUM -> {
                return 80000;
            }
        }
        return 0;
    }
}
