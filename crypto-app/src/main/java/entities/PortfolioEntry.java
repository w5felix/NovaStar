package entities;

/**
 * Represents a portfolio entry, which tracks the amount of a specific cryptocurrency held by a user.
 */
public class PortfolioEntry {

    private String cryptoName; // Name of the cryptocurrency (e.g., "BTC")
    private double amount;     // Amount of cryptocurrency held

    /**
     * Constructor to initialize a portfolio entry.
     *
     * @param cryptoName Name of the cryptocurrency (e.g., "BTC").
     * @param amount     Initial amount of the cryptocurrency (must be non-negative).
     */
    public PortfolioEntry(String cryptoName, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative.");
        }
        this.cryptoName = cryptoName;
        this.amount = amount;
    }

    /**
     * @return The name of the cryptocurrency.
     */
    public String getCryptoName() {
        return cryptoName;
    }

    /**
     * @return The amount of cryptocurrency held.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Adds an amount to the current cryptocurrency holdings.
     *
     * @param amount The amount to add (must be positive).
     * @throws IllegalArgumentException if the amount is non-positive.
     */
    public void addAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be positive.");
        }
        this.amount += amount;
    }

    /**
     * Subtracts an amount from the current cryptocurrency holdings.
     *
     * @param amount The amount to subtract (must be positive and not greater than the current amount).
     * @throws IllegalArgumentException if the amount is non-positive or greater than the current holdings.
     */
    public void subtractAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to subtract must be positive.");
        }
        if (amount > this.amount) {
            throw new IllegalArgumentException("Amount to subtract exceeds current holdings.");
        }
        this.amount -= amount;
    }

    /**
     * Returns a string representation of the portfolio entry, useful for logging or debugging.
     *
     * @return A string containing the cryptocurrency name and amount held.
     */
    @Override
    public String toString() {
        return "PortfolioEntry{" +
                "cryptoName='" + cryptoName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
