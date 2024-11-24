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
     * Constructor to initialize a user object by fetching data from Firebase.
     *
     * @param userId The unique ID of the user.
     * @param name   The name of the user.
     * @throws IOException if there's an error fetching user data from Firebase.
     */
    public User(String userId, String name) throws IOException {
        this.userId = userId;
        this.name = name;
        this.cashBalance = FireBaseAPIClient.getCashReserves(userId); // Fetch initial balance from Firebase
        this.transactions = FireBaseAPIClient.getTransactions(userId); // Fetch transactions from Firebase
        this.portfolio = FireBaseAPIClient.getPortfolioEntries(userId); // Fetch portfolio from Firebase
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
}
