package entities;

import java.util.List;

/**
 * Represents a user in the system, holding data such as their unique ID, username,
 * cash balance, portfolio, and transaction history.
 *
 * This class is a pure data object and does not handle any business logic.
 */
public class User {

    private final String userId;
    private String username;
    private String email;
    private double cashBalance;
    private List<Transaction> transactions;
    private List<PortfolioEntry> portfolio;

    /**
     * Constructor to initialize the user object with required data.
     *
     * @param userId      Unique ID for the user.
     * @param username    Username of the user.
     * @param cashBalance Initial cash balance of the user.
     * @param transactions List of the user's past transactions.
     * @param portfolio   List of the user's portfolio entries.
     */
    public User(String userId, String username, double cashBalance,
                List<Transaction> transactions, List<PortfolioEntry> portfolio) {
        this.userId = userId;
        this.username = username;
        this.cashBalance = cashBalance;
        this.transactions = transactions;
        this.portfolio = portfolio;
    }

    /**
     * @return The unique ID of the user.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     *
     * @param username The new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The cash balance of the user.
     */
    public double getCashBalance() {
        return cashBalance;
    }

    /**
     * Sets the user's cash balance.
     *
     * @param cashBalance The new cash balance.
     */
    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    /**
     * @return The user's transaction history.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Sets the user's transaction history.
     *
     * @param transactions The list of transactions to set.
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * @return The user's portfolio.
     */
    public List<PortfolioEntry> getPortfolio() {
        return portfolio;
    }

    /**
     * Sets the user's portfolio.
     *
     * @param portfolio The list of portfolio entries to set.
     */
    public void setPortfolio(List<PortfolioEntry> portfolio) {
        this.portfolio = portfolio;
    }



    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", cashBalance=" + cashBalance +
                ", transactions=" + transactions +
                ", portfolio=" + portfolio +
                '}';
    }

}
