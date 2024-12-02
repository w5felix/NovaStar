package domain.entities;

import java.util.Date;

/**
 * Represents a cryptocurrency transaction, including details such as the type (BUY or SELL),
 * cryptocurrency name, amount, price, and transaction date.
 */
public class Transaction {

    private int id; // Unique ID for the transaction (used when retrieved from a database)
    private String cryptoName; // Name of the cryptocurrency (e.g., "BTC")
    private double amount; // Amount of cryptocurrency in the transaction
    private double price; // Price per unit at the time of the transaction
    private Date date; // Date of the transaction
    private String type; // Type of transaction: BUY or SELL

    /**
     * Constructor for creating a new transaction.
     *
     * @param cryptoName Name of the cryptocurrency.
     * @param amount     Amount of cryptocurrency involved in the transaction (must be positive).
     * @param price      Price per unit of the cryptocurrency (must be positive).
     * @param date       Date of the transaction.
     * @param type       Type of transaction: BUY or SELL.
     * @throws IllegalArgumentException if the amount or price is non-positive,
     *                                  or if the type is not BUY or SELL.
     */
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
        this.type = type.toUpperCase(); // Standardize type to uppercase for consistency
    }

    /**
     * Constructor for creating a transaction retrieved from a database.
     *
     * @param id         Unique identifier for the transaction.
     * @param cryptoName Name of the cryptocurrency.
     * @param amount     Amount of cryptocurrency involved in the transaction.
     * @param price      Price per unit of the cryptocurrency.
     * @param date       Date of the transaction.
     * @param type       Type of transaction: BUY or SELL.
     */
    public Transaction(int id, String cryptoName, double amount, double price, Date date, String type) {
        this(cryptoName, amount, price, date, type); // Reuse validation logic from the primary constructor
        this.id = id; // Set the unique ID
    }

    // Getters

    /**
     * @return The unique ID of the transaction.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The name of the cryptocurrency involved in the transaction.
     */
    public String getCryptoName() {
        return cryptoName;
    }

    /**
     * @return The amount of cryptocurrency involved in the transaction.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @return The price per unit of the cryptocurrency at the time of the transaction.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return The date of the transaction.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return The type of the transaction (BUY or SELL).
     */
    public String getType() {
        return type;
    }

    // Setters (if necessary)

    /**
     * Sets the unique ID for the transaction.
     * This is typically used when the transaction is saved in or retrieved from a database.
     *
     * @param id The unique ID of the transaction.
     */
    public void setId(int id) {
        this.id = id;
    }

    // Override toString

    /**
     * Returns a string representation of the transaction, useful for logging or debugging.
     *
     * @return A string containing the transaction details.
     */
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
