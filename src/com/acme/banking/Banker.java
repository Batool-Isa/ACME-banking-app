package com.acme.banking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

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

    public void saveBankerOperations(Customer customer) {

        File folder = new File("data/bankerFiles");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File customerFile = new File(
                folder,
                "Banker-" + this.getFullName() + "-" + this.getBankerId()
        );
        try {
            if (!customerFile.exists()) {
                customerFile.createNewFile();
            }

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(customerFile, true));

            writer.write("Customer ID: " + customer.getCustomerId());
            writer.newLine();
            writer.write("Customer Full Name: " + customer.getFullName());
            writer.newLine();

            writer.write("Customer Username: " + customer.getUsername());
            writer.newLine();

            Account acc = customer.getAccounts().stream().max(Comparator.comparing(a->a.getCreatedAt())).orElse(null);
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String formattedTx = acc.getCreatedAt().format(formatter);

            writer.write("Date: " + formattedTx);
            writer.newLine();

            writer.write("----------------------------------------");
            writer.newLine();

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
