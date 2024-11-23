package entities;

import java.util.Date;

public class Transaction {
    private final String cryptoID;
    private final double amount; // Amount of cryptocurrency
    private final double price;  // Price per unit at the time of transaction
    private final Date date;

    public Transaction(String cryptoID, double amount, double price, Date date) {
        this.cryptoID = cryptoID;
        this.amount = amount;
        this.price = price;
        this.date = date;
    }

    public String getCryptoID() {
        return cryptoID;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }
}
