package entities;

import api.BlockChainAPIClient;
import api.FireBaseAPIClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String userId; // Unique identifier for the user
    private String name;   // User's name
    private double cashBalance; // User's cash balance (sync with Firebase)
    private List<Transaction> transactions; // User's transaction history
    private List<PortfolioEntry> portfolio; // Portfolio holdings

    public User(String userId, String name) throws IOException {
        this.userId = userId;
        this.name = name;
        this.cashBalance = FireBaseAPIClient.getCashReserves(userId); // Fetch initial balance from Firebase
        this.transactions = FireBaseAPIClient.getTransactions(userId); // Fetch transactions from Firebase
        this.portfolio = FireBaseAPIClient.getPortfolioEntries(userId); // Fetch portfolio from Firebase
    }

    // Buy cryptocurrency
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


    // Sell cryptocurrency
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

    // Deposit cash
    public void depositCash(double amount) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        cashBalance += amount;
        FireBaseAPIClient.addCash(userId, amount); // Update Firebase cash balance
    }

    // Withdraw cash
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

    // Calculate total portfolio value
    public double calculatePortfolioValue() throws Exception {
        double totalValue = cashBalance;

        for (PortfolioEntry entry : getPortfolio()) {
            String symbol = entry.getCryptoName() + "-USD";
            double price = BlockChainAPIClient.getCurrentPrice(symbol);
            totalValue += entry.getAmount() * price;
        }
        return totalValue;
    }

    // Refresh portfolio from Firebase
    private void refreshPortfolio() throws IOException {
        this.portfolio = FireBaseAPIClient.getPortfolioEntries(userId);
    }

    // Refresh transactions from Firebase
    private void refreshTransactions() throws IOException {
        this.transactions = FireBaseAPIClient.getTransactions(userId);
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public double getCashBalance() throws IOException {
        // Ensure we fetch the latest balance from Firebase
        this.cashBalance = FireBaseAPIClient.getCashReserves(userId);
        return cashBalance;
    }

    public List<Transaction> getTransactions() throws IOException {
        refreshTransactions();
        return transactions;
    }

    public List<PortfolioEntry> getPortfolio() throws IOException {
        refreshPortfolio();
        return portfolio;
    }
}
