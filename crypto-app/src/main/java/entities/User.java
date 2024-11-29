package entities;

import api.BlockChainAPIClient;
import api.FireBaseAPIClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a user with their associated financial data, such as cash balance, transaction history, and portfolio.
 * Provides methods for buying/selling cryptocurrency, managing cash, and calculating portfolio value.
 */
public class User {

    private String userId; // Unique identifier for the user
    private String name;   // User's name
    private double cashBalance; // User's cash balance (sync with Firebase)
    private List<Transaction> transactions; // User's transaction history
    private List<PortfolioEntry> portfolio; // Portfolio holdings

    /**
     * Constructor to initialize a user object using email and password.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @throws IOException if there's an error logging in or fetching user data from Firebase.
     */
    public User(String email, String password) throws IOException {
        // Authenticate the user with Firebase
        this.userId = FireBaseAPIClient.loginUser(email, password);
        // Fetch user details (name, cash reserves, transactions, and portfolio) from Firebase
        this.name = email; // Defaulting name to email if no separate name is stored
        this.cashBalance = FireBaseAPIClient.getCashReserves(userId); // Fetch initial balance from Firebase
        this.transactions = FireBaseAPIClient.getTransactions(userId); // Fetch transactions from Firebase
        this.portfolio = FireBaseAPIClient.getPortfolioEntries(userId); // Fetch portfolio from Firebase
    }

    /**
     * Constructor to register and initialize a user object.
     *
     * @param username             The user's username.
     * @param email                The user's email address.
     * @param password             The user's chosen password.
     * @param securityQuestion     The user's security question for recovery.
     * @param securityQuestionAnswer The user's answer to the security question.
     * @throws IOException if there's an error during registration or fetching data from Firebase.
     */
    public User(String username, String email, String password, String securityQuestion, String securityQuestionAnswer) throws IOException {
        // Step 1: Register the user and get the unique ID
        this.userId = FireBaseAPIClient.registerUser(username, email, password, securityQuestion, securityQuestionAnswer);
        this.name = username;

        // Step 2: Initialize the rest of the user's details
        this.cashBalance = FireBaseAPIClient.getCashReserves(userId); // Initialize with 0.0 or the default value
        this.transactions = FireBaseAPIClient.getTransactions(userId); // Fetch an empty transaction history initially
        this.portfolio = FireBaseAPIClient.getPortfolioEntries(userId); // Fetch an empty portfolio initially
    }


    /**
     * Buys a specified amount of cryptocurrency.
     *
     * @param cryptoName The name of the cryptocurrency (e.g., "BTC").
     * @param amount     The amount to buy.
     * @throws Exception if there's an error fetching prices or updating the portfolio.
     */
    public void buyCrypto(String cryptoName, double amount) throws Exception {
        String symbol = cryptoName + "-USD"; // Assuming USD trading pair
        double price = BlockChainAPIClient.getCurrentPrice(symbol);
        double cost = amount * price;

        if (cashBalance < cost) {
            throw new IllegalArgumentException("Insufficient cash balance to buy " + cryptoName);
        }

        cashBalance -= cost; // Deduct cash only after Firebase operation succeeds

        // Create and save transaction
        Transaction transaction = new Transaction(cryptoName, amount, price, new Date(), "BUY");
        FireBaseAPIClient.addTransactionAndUpdatePortfolio(userId, transaction); // Save to Firebase

        refreshPortfolio(); // Ensure portfolio is updated
        refreshTransactions(); // Update transactions
    }

    /**
     * Sells a specified amount of cryptocurrency.
     *
     * @param cryptoName The name of the cryptocurrency (e.g., "BTC").
     * @param amount     The amount to sell.
     * @throws Exception if there's an error fetching prices or updating the portfolio.
     */
    public void sellCrypto(String cryptoName, double amount) throws Exception {
        PortfolioEntry portfolioEntry = FireBaseAPIClient.getPortfolioEntry(userId, cryptoName);
        if (portfolioEntry == null || portfolioEntry.getAmount() < amount) {
            throw new IllegalArgumentException("Insufficient holdings to sell " + cryptoName);
        }

        String symbol = cryptoName + "-USD"; // Assuming USD trading pair
        double price = BlockChainAPIClient.getCurrentPrice(symbol);
        double revenue = amount * price;

        cashBalance += revenue;

        Transaction transaction = new Transaction(cryptoName, amount, price, new Date(), "SELL");
        FireBaseAPIClient.addTransactionAndUpdatePortfolio(userId, transaction); // Save to Firebase

        refreshPortfolio(); // Ensure portfolio is updated
        refreshTransactions(); // Update transactions
    }

    /**
     * Deposits cash into the user's account.
     *
     * @param amount The amount to deposit.
     * @throws IOException              if there's an error updating the balance in Firebase.
     * @throws IllegalArgumentException if the deposit amount is non-positive.
     */
    public void depositCash(double amount) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        cashBalance += amount;
        FireBaseAPIClient.addCash(userId, amount); // Update Firebase cash balance
    }

    /**
     * Withdraws cash from the user's account.
     *
     * @param amount The amount to withdraw.
     * @throws IOException              if there's an error updating the balance in Firebase.
     * @throws IllegalArgumentException if the withdrawal amount is non-positive or exceeds the current balance.
     */
    public void withdrawCash(double amount) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (cashBalance < amount) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal.");
        }
        cashBalance -= amount;
        FireBaseAPIClient.withdrawCash(userId, amount); // Update Firebase cash balance
    }

    /**
     * Calculates the total value of the user's portfolio, including cash reserves.
     *
     * @return The total portfolio value in USD.
     * @throws Exception if there's an error fetching cryptocurrency prices.
     */
    public double calculatePortfolioValue() throws Exception {
        double totalValue = cashBalance;

        for (PortfolioEntry entry : getPortfolio()) {
            String symbol = entry.getCryptoName() + "-USD";
            double price = BlockChainAPIClient.getCurrentPrice(symbol);
            totalValue += entry.getAmount() * price;
        }
        return totalValue;
    }

    /**
     * Refreshes the user's portfolio by fetching the latest data from Firebase.
     *
     * @throws IOException if there's an error fetching the portfolio data.
     */
    private void refreshPortfolio() throws IOException {
        this.portfolio = FireBaseAPIClient.getPortfolioEntries(userId);
    }

    /**
     * Refreshes the user's transaction history by fetching the latest data from Firebase.
     *
     * @throws IOException if there's an error fetching the transactions data.
     */
    private void refreshTransactions() throws IOException {
        this.transactions = FireBaseAPIClient.getTransactions(userId);
    }

    // Getters

    /**
     * @return The user's unique ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Fetches the user's cash balance, ensuring it's up-to-date with Firebase.
     *
     * @return The user's current cash balance.
     * @throws IOException if there's an error fetching the balance from Firebase.
     */
    public double getCashBalance() throws IOException {
        this.cashBalance = FireBaseAPIClient.getCashReserves(userId); // Sync balance from Firebase
        return cashBalance;
    }

    /**
     * Fetches the user's transaction history, ensuring it's up-to-date with Firebase.
     *
     * @return A list of the user's transactions.
     * @throws IOException if there's an error fetching the transactions from Firebase.
     */
    public List<Transaction> getTransactions() throws IOException {
        refreshTransactions();
        return transactions;
    }

    /**
     * Fetches the user's portfolio holdings, ensuring they're up-to-date with Firebase.
     *
     * @return A list of the user's portfolio entries.
     * @throws IOException if there's an error fetching the portfolio from Firebase.
     */
    public List<PortfolioEntry> getPortfolio() throws IOException {
        refreshPortfolio();
        return portfolio;
    }

    /**
     * Adds a new alert for the user.
     *
     * @param cryptoName  The name of the cryptocurrency.
     * @param targetPrice The target price for the alert.
     * @param isAbove     True if the alert triggers when price exceeds target.
     * @throws IOException If there is an issue saving the alert in Firebase.
     */
    public void addAlert(String cryptoName, double targetPrice, boolean isAbove) throws IOException {
        // Create a new alert with the current user's userId
        Alert alert = new Alert(this.userId, cryptoName, targetPrice, isAbove);

        // Pass userId explicitly to saveAlert
        FireBaseAPIClient.saveAlert(this.userId, alert);
    }

    /**
     * Fetches all alerts for the user.
     *
     * @return A list of alerts.
     * @throws IOException If there is an issue fetching alerts from Firebase.
     */
    public List<Alert> getAlerts() throws IOException {
        // Fetch all alerts for the current user using userId
        return FireBaseAPIClient.getUserAlerts(this.userId);
    }
}

