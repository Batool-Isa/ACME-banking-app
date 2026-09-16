package com.acme.banking;

import java.time.LocalDate;
import java.util.Random;

public class Card {
    private int cardId;
    private CardType type;
    private String cardNumber;
    private LocalDate dateIssued;
    private LocalDate expiryDate;
    private String cvv;
    private int accountId;
    int idStart = 100;

    public enum CardType {
        MASTERCARD,
        MASTERCARD_TITANIUM,
        MASTERCARD_PLATINUM
    }

    public Card(CardType type) {
        idStart++;
        this.cardId = idStart;
        this.type = type;
        this.cardNumber = generateRandomCardNumber();
        this.dateIssued = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(5);
        this.cvv = generateRandomCVV();
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public static String generateRandomCardNumber(){
        Random random = new Random();
        StringBuilder cardNum = new StringBuilder();
        for(int i=0; i < 16 ;i++){
            cardNum.append(random.nextInt(10));
        }
        return cardNum.toString();
    }
    public static String generateRandomCVV(){
        Random random = new Random();
        StringBuilder cvv = new StringBuilder();
        for(int i=0; i < 3 ;i++){
            cvv.append(random.nextInt(10));
        }
        return cvv.toString();
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
