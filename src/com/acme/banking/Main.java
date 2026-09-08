package com.acme.banking;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        Bank bank = new Bank();
        bank.loadUsers();
        System.out.println("Welcome to ACME Banking App!");
        System.out.println("Choose the operation you want to do:");
        System.out.println("1- Login");
        System.out.println("2- SignUp");
        Scanner scan = new Scanner(System.in);
        int userInput = scan.nextInt();
        scan.nextLine();
        switch (userInput) {
            case 1:
                System.out.println("Enter Username:");
                String username = scan.nextLine();
                System.out.println("Enter password");
                String pass = scan.nextLine();
                System.out.println();
                break;
            case 2:
                System.out.println("Enter Username:");
                String customerUsername = scan.nextLine();
                System.out.println("Enter your first name:");
                String firstName = scan.nextLine();
                System.out.println("Enter your last name:");
                String lastName = scan.nextLine();
                System.out.println("Enter password");
                String password = scan.nextLine();
                Customer customer  = new Customer(firstName,lastName,customerUsername,password,"C");
                bank.addUser(customer);
                System.out.println("Congrats, Your Account Created Successfully!");
                System.out.println("Your username: "+customer.getUsername()+" & customer Id: "+customer.getCustomerId());
                break;
            default:
                System.out.println("Invalid Input");
                break;
        }


scan.close();

    }

}
