package entities;

import java.util.Date;

public class Transaction {
    private int id; // Unique ID for the transaction
    private String cryptoName; // Name of the cryptocurrency
    private double amount; // Amount of cryptocurrency
    private double price;  // Price per unit at the time of transaction
    private Date date; // Date of the transaction
    private String type; // Type of transaction: BUY or SELL

    // Constructor for new transactions
    public Transaction(String cryptoName, double amount, double price, Date date, String type) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        if (!type.equalsIgnoreCase("BUY") && !type.equalsIgnoreCase("SELL")) {
            throw new IllegalArgumentException("Transaction type must be BUY or SELL.");
        }

        this.cryptoName = cryptoName;
        this.amount = amount;
        this.price = price;
        this.date = date;
        this.type = type.toUpperCase(); // Standardize type to uppercase
    }

    // Constructor for database retrieval
    public Transaction(int id, String cryptoName, double amount, double price, Date date, String type) {
        this(cryptoName, amount, price, date, type); // Reuse validation logic
        this.id = id; // ID is set only when retrieved from DB
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCryptoName() {
        return cryptoName;
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

    public String getType() {
        return type;
    }

    // Setters (if necessary)
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", cryptoName='" + cryptoName + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", date=" + date +
                ", type='" + type + '\'' +
                '}';
    }
}

