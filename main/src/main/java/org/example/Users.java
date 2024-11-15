package org.example;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private String username;
    private Wallet wallet;
    private String preferredCurrency;
    private List<Transaction> transactionHistory;

    public void User(String username, String preferredCurrency) {
        this.username = username;
        this.preferredCurrency = preferredCurrency;
        this.wallet = new Wallet();
        this.transactionHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(Transaction transaction) {
        this.transactionHistory.add(transaction);
    }

    public void displayUserDetails() {
        System.out.println("Username: " + username);
        System.out.println("Preferred currency: " + preferredCurrency);
        System.out.println("Wallet: " + wallet.toString());
        System.out.println("Transaction history: ");
        for (Transaction transaction : transactionHistory) {
        }
    }

    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", preferredCurrency='" + preferredCurrency + '\'' +
                ", wallet=" + wallet +
                ", transactionHistory=" + transactionHistory +
                '}';
    }
}
