package com.acme.banking;

import java.time.LocalDateTime;

public abstract class User implements IBankingOperations{
    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private int failedLoginAttempts;
    private LocalDateTime lockedUntil;
    private static int idStart = 1006;
    public User(String firstName, String lastName, String username, String password, String role) {
        idStart++;
        this.userId = idStart;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = SecurityUtil.hashPassword(password);
        this.role = role;
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public boolean checkPassword(String pass){
        return SecurityUtil.verifyPassword(pass, getPassword());
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        if (role.equals("B")){
            return "Banker";
        }else {
            return "Customer";
        }
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public boolean login(String username, String pass){

        return (username.equals(this.username) && pass.equals(this.password));
    }
    public static void validateName(String name){
    if (name.length() < 3){
        System.out.println("Name Can't be less than 3 characters!");
    }
    }


    @Override
    public String toString() {
        return "User ID: " + userId +
                ", Name: " + firstName + " " + lastName +
                ", Username: " + username +
                ", Role: " + (role);
    }
    public String getFullName() {
        return  firstName + " " + lastName ;
    }
}
