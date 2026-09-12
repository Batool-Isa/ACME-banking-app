package com.acme.banking;

public class Banker extends User {
    private int bankerId;
    private static int idStart = 3000;
    public Banker(String firstName, String lastName, String username, String password, String role) {
        super(firstName, lastName, username, password,role);
        idStart++;
        this.bankerId = idStart;
    }

    public int getBankerId() {
        return bankerId;
    }

    public void setBankerId(int bankerId) {
        this.bankerId = bankerId;
    }


        @Override
    public double deposit(Account acc, double amount) {
        return 0;
    }

    @Override
    public double withdraw(Account acc, double amount) {
return 0;
    }

    @Override
    public void transferMoney(double amount, int srcAccount, int destinationAccount) {

    }
}
