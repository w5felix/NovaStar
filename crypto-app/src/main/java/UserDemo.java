import api.FireBaseAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This demo class directly interacts with the `User` class without a command-line interface.
 * It demonstrates creating users and performing various operations programmatically.
 *
 * Each operation is explained with comments, and the APIs being called in the background
 * (e.g., `FireBaseAPIClient` and `BlockChainAPIClient`) are highlighted for clarity.
 */
public class UserDemo {

    public static void main(String[] args) {
        try {
            // STEP 1: Create a new user with a unique email
            System.out.println("Creating a new user...");
            String uniqueEmail = generateUniqueEmail(); // Generate an email based on the current date and time
            String password = "password123"; // Default password for the demo
            String userId = FireBaseAPIClient.registerUser(uniqueEmail, password);
            User user = new User(userId, "Test User");
            System.out.println("User created successfully! Email: " + uniqueEmail);
            System.out.println("User ID: " + userId);

            // STEP 2: Deposit a larger initial balance ($10,000)
            System.out.println("\nDepositing $10,000 into the user's account...");
            user.depositCash(1000000);
            System.out.println("Cash deposited successfully!");
            System.out.printf("Updated cash reserves: $%.2f%n", user.getCashBalance());

            // STEP 3: Buy cryptocurrency
            System.out.println("\nBuying 0.5 BTC...");
            user.buyCrypto("BTC", 0.5);
            System.out.println("Cryptocurrency purchased successfully!");
            System.out.printf("Updated cash reserves: $%.2f%n", user.getCashBalance());
            displayPortfolio(user);

            // STEP 4: Sell cryptocurrency
            System.out.println("\nSelling 0.25 BTC...");
            user.sellCrypto("BTC", 0.25);
            System.out.println("Cryptocurrency sold successfully!");
            System.out.printf("Updated cash reserves: $%.2f%n", user.getCashBalance());
            displayPortfolio(user);

            // STEP 5: View transaction history
            System.out.println("\nFetching transaction history...");
            displayTransactions(user);

            // STEP 6: Calculate total portfolio value
            System.out.println("\nCalculating total portfolio value...");
            double portfolioValue = user.calculatePortfolioValue();
            System.out.printf("Total portfolio value (cash + holdings): $%.2f%n", portfolioValue);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Generates a unique email address based on the current date and time.
     * Ensures that each user created during the demo has a unique email.
     *
     * Example output: user_20241124_153045@example.com
     *
     * @return A unique email address.
     */
    private static String generateUniqueEmail() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "user_" + timestamp + "@example.com";
    }

    /**
     * Helper method to display the user's portfolio.
     * Uses `User.getPortfolio`, which fetches data from Firebase via `FireBaseAPIClient.getPortfolioEntries`.
     *
     * Example output:
     * Cryptocurrency: BTC, Amount: 0.250000
     * Cryptocurrency: ETH, Amount: 0.500000
     */
    private static void displayPortfolio(User user) throws IOException {
        System.out.println("\nCurrent Portfolio:");
        List<PortfolioEntry> portfolio = user.getPortfolio();

        if (portfolio.isEmpty()) {
            System.out.println("No cryptocurrency holdings found.");
        } else {
            for (PortfolioEntry entry : portfolio) {
                System.out.printf("Cryptocurrency: %s, Amount: %.6f%n", entry.getCryptoName(), entry.getAmount());
            }
        }
    }

    /**
     * Helper method to display the user's transaction history.
     * Uses `User.getTransactions`, which fetches transaction data from Firebase via `FireBaseAPIClient.getTransactions`.
     *
     * Example output:
     * Transaction{id=1, cryptoName='BTC', amount=0.5, price=50000.0, date=..., type='BUY'}
     * Transaction{id=2, cryptoName='BTC', amount=0.25, price=50000.0, date=..., type='SELL'}
     */
    private static void displayTransactions(User user) throws IOException {
        List<Transaction> transactions = user.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }
}
