package services;

import api.BlockChainAPIClient;
import api.FireBaseAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Service class for handling all user-related business logic and operations.
 * Interacts with FireBaseAPIClient and BlockChainAPIClient for backend operations.
 */
public class UserService {

    private final FireBaseAPIClient firebaseClient;
    private final BlockChainAPIClient cryptoClient;

    /**
     * Constructor for initializing the service with required API clients.
     *
     * @param firebaseClient The Firebase API client.
     * @param cryptoClient   The Blockchain API client.
     */
    public UserService(FireBaseAPIClient firebaseClient, BlockChainAPIClient cryptoClient) {
        this.firebaseClient = firebaseClient;
        this.cryptoClient = cryptoClient;
    }

    /**
     * Logs in a user and initializes their User object.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return The initialized User object.
     * @throws IOException If login fails or data retrieval encounters an issue.
     */
    public User loginUser(String email, String password) throws IOException {
        String userId = firebaseClient.loginUser(email, password);
        double cashBalance = firebaseClient.getCashReserves(userId);
        List<Transaction> transactions = firebaseClient.getTransactions(userId);
        List<PortfolioEntry> portfolio = firebaseClient.getPortfolioEntries(userId);

        return new User(userId, email, cashBalance, transactions, portfolio);
    }

    /**
     * Registers a new user and initializes their User object.
     *
     * @param username             The user's username.
     * @param email                The user's email.
     * @param password             The user's password.
     * @param securityQuestion     The user's security question for recovery.
     * @param securityQuestionAnswer The answer to the user's security question.
     * @return The initialized User object.
     * @throws IOException If registration fails or data retrieval encounters an issue.
     */
    public User registerUser(String username, String email, String password,
                             String securityQuestion, String securityQuestionAnswer) throws IOException {
        String userId = firebaseClient.registerUser(username, email, password, securityQuestion, securityQuestionAnswer);
        double cashBalance = firebaseClient.getCashReserves(userId);
        List<Transaction> transactions = firebaseClient.getTransactions(userId);
        List<PortfolioEntry> portfolio = firebaseClient.getPortfolioEntries(userId);

        return new User(userId, username, cashBalance, transactions, portfolio);
    }

    /**
     * Deposits cash into a user's account.
     *
     * @param user   The User object.
     * @param amount The amount to deposit.
     * @throws IOException If the operation fails.
     */
    public void depositCash(User user, double amount) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        firebaseClient.addCash(user.getUserId(), amount);
        user.setCashBalance(user.getCashBalance() + amount);
    }

    /**
     * Withdraws cash from a user's account.
     *
     * @param user   The User object.
     * @param amount The amount to withdraw.
     * @throws IOException              If the operation fails.
     * @throws IllegalArgumentException If the amount exceeds the user's balance.
     */
    public void withdrawCash(User user, double amount) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (user.getCashBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal.");
        }
        firebaseClient.withdrawCash(user.getUserId(), amount);
        user.setCashBalance(user.getCashBalance() - amount);
    }

    /**
     * Executes a cryptocurrency purchase for the user.
     *
     * @param user       The User object.
     * @param cryptoName The name of the cryptocurrency (e.g., "BTC").
     * @param amount     The amount to purchase.
     * @throws Exception If the operation fails.
     */
    public void buyCrypto(User user, String cryptoName, double amount) throws Exception {
        double price = cryptoClient.getCurrentPrice(cryptoName + "-USD");
        double cost = amount * price;

        if (user.getCashBalance() < cost) {
            throw new IllegalArgumentException("Insufficient cash balance to buy " + cryptoName);
        }

        firebaseClient.addTransactionAndUpdatePortfolio(user.getUserId(),
                new Transaction(cryptoName, amount, price, new Date(), "BUY"));

        // Update user data
        user.setCashBalance(user.getCashBalance() - cost);
        refreshPortfolio(user);
    }

    /**
     * Executes a cryptocurrency sale for the user.
     *
     * @param user       The User object.
     * @param cryptoName The name of the cryptocurrency (e.g., "BTC").
     * @param amount     The amount to sell.
     * @throws Exception If the operation fails.
     */
    public void sellCrypto(User user, String cryptoName, double amount) throws Exception {
        PortfolioEntry entry = user.getPortfolio().stream()
                .filter(e -> e.getCryptoName().equals(cryptoName))
                .findFirst()
                .orElse(null);

        if (entry == null || entry.getAmount() < amount) {
            throw new IllegalArgumentException("Insufficient holdings to sell " + cryptoName);
        }

        double price = cryptoClient.getCurrentPrice(cryptoName + "-USD");
        double revenue = amount * price;

        firebaseClient.addTransactionAndUpdatePortfolio(user.getUserId(),
                new Transaction(cryptoName, amount, price, new Date(), "SELL"));

        // Update user data
        user.setCashBalance(user.getCashBalance() + revenue);
        refreshPortfolio(user);
    }

    /**
     * Refreshes the user's portfolio data from Firebase.
     *
     * @param user The User object.
     * @throws IOException If the operation fails.
     */
    public void refreshPortfolio(User user) throws IOException {
        user.setPortfolio(firebaseClient.getPortfolioEntries(user.getUserId()));
    }

    /**
     * Refreshes the user's transaction history from Firebase.
     *
     * @param user The User object.
     * @throws IOException If the operation fails.
     */
    public void refreshTransactions(User user) throws IOException {
        user.setTransactions(firebaseClient.getTransactions(user.getUserId()));
    }

    /**
     * Resets a user's password.
     *
     * @param email The email address of the user.
     * @throws IOException If the operation fails.
     */
    public void resetPassword(String email) throws IOException {
        firebaseClient.resetPassword(email);
    }

    /**
     * Updates the user's profile information (username and email).
     *
     * @param user        The User object.
     * @param newUsername The new username.
     * @param newEmail    The new email.
     * @throws IOException If the operation fails.
     */
    public void updateUserProfile(User user, String newUsername, String newEmail) throws IOException {
        firebaseClient.updateUserProfile(user.getUserId(), newUsername, newEmail);
        user.setUsername(newUsername);
        user.setEmail(newEmail);
    }

    /**
     * Calculates the total value of the user's portfolio, including cash reserves.
     *
     * @param user The User object.
     * @return The total portfolio value.
     * @throws Exception If the operation fails.
     */
    public double calculatePortfolioValue(User user) throws Exception {
        double totalValue = 0.0;

        for (PortfolioEntry entry : user.getPortfolio()) {
            double currentPrice = cryptoClient.getCurrentPrice(entry.getCryptoName() + "-USD");
            totalValue += entry.getAmount() * currentPrice;
        }

        return totalValue + user.getCashBalance();
    }

    /**
     * Gets the current value of a specific holding in the user's portfolio.
     *
     * @param entry The PortfolioEntry object.
     * @return The current value of the holding.
     * @throws Exception If the operation fails.
     */
    public double getCurrentValueOfHolding(PortfolioEntry entry) throws Exception {
        double currentPrice = cryptoClient.getCurrentPrice(entry.getCryptoName() + "-USD");
        return entry.getAmount() * currentPrice;
    }

    /**
     * Gets the total initial investment for a specific cryptocurrency in the user's portfolio.
     *
     * @param user  The User object.
     * @param entry The PortfolioEntry object.
     * @return The total initial investment.
     * @throws Exception If the operation fails.
     */
    public double getInitialInvestment(User user, PortfolioEntry entry) throws Exception {
        double totalInvestment = 0.0;

        for (Transaction transaction : user.getTransactions()) {
            if (transaction.getCryptoName().equalsIgnoreCase(entry.getCryptoName())
                    && transaction.getType().equalsIgnoreCase("BUY")) {
                totalInvestment += transaction.getAmount() * transaction.getPrice();
            }
        }

        return totalInvestment;
    }
}
