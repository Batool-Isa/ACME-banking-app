package com.acme.banking;

import java.time.LocalDate;

public class Card {
private int cardId;
private CardType type;
private int cardNumber;
private LocalDate dateIssued;
private LocalDate expiryDate;
private int cvs;
int idStart = 100;
    public enum CardType {
        MASTERCARD,
        MASTERCARD_TITANIUM,
        MASTERCARD_PLATINUM
    }

    public Card(int cardId, CardType type, int cardNumber, int cvs) {
        idStart++;
        this.cardId = idStart;
        this.type = type;
        this.cardNumber = cardNumber;
        this.dateIssued = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(5);
        this.cvs = cvs;
    }
}
