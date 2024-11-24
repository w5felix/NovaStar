import api.FireBaseAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * This is the main entry point for our Crypto Portfolio Management System.
 * It's a command-line program where users can register, log in, and manage their cryptocurrency portfolio.
 *
 * Main functionalities include:
 * - Registering and logging in users (with Firebase authentication).
 * - Viewing and managing cash reserves.
 * - Buying and selling cryptocurrencies.
 * - Viewing transaction history and portfolio details.
 *
 * This class interacts heavily with:
 * - `FireBaseAPIClient`: Handles communication with the Firebase backend for user data, portfolio, and transactions.
 * - `User`: Represents the logged-in user and provides methods to perform portfolio actions.
 * - `PortfolioEntry` and `Transaction`: Represent portfolio holdings and individual transactions, respectively.
 */
public class CommandLineDemo {
    private static Scanner scanner = new Scanner(System.in); // Scanner for user input
    private static User currentUser; // Stores the currently logged-in user

    public static void main(String[] args) {
        System.out.println("Welcome to the Crypto Portfolio Management System!");

        // The program will keep running until the user chooses to exit.
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine()); // Read user's choice

            switch (choice) {
                case 1:
                    handleRegister(); // User wants to register
                    break;
                case 2:
                    handleLogin(); // User wants to log in
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handles user registration.
     * - Prompts the user to enter their email and password.
     * - Registers the user using `FireBaseAPIClient.registerUser`.
     * - Displays their unique User ID upon success.
     *
     * Example interaction:
     * User: Enter your email: alice@example.com
     * User: Enter your password: password123
     * System: Registration successful! Your User ID: abcd1234
     */
    private static void handleRegister() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine(); // Get user's email
            System.out.print("Enter your password: ");
            String password = scanner.nextLine(); // Get user's password

            // Use the Firebase API to register the user
            String userId = FireBaseAPIClient.registerUser(email, password);
            System.out.println("Registration successful! Your User ID: " + userId);
        } catch (IOException e) {
            System.err.println("Error during registration: " + e.getMessage());
        }
    }

    /**
     * Handles user login.
     * - Prompts the user for their email and password.
     * - Logs in using `FireBaseAPIClient.loginUser`.
     * - Initializes the `currentUser` object with data from Firebase.
     * - Grants access to user actions upon successful login.
     *
     * Example interaction:
     * User: Enter your email: alice@example.com
     * User: Enter your password: password123
     * System: Enter your name: Alice
     * System: Login successful! Welcome, Alice
     */
    private static void handleLogin() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine(); // Get user's email
            System.out.print("Enter your password: ");
            String password = scanner.nextLine(); // Get user's password

            // Authenticate the user via Firebase
            String userId = FireBaseAPIClient.loginUser(email, password);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine(); // Get the user's name for display purposes

            // Initialize the currentUser object, which fetches their data from Firebase
            currentUser = new User(userId, name);
            System.out.println("Login successful! Welcome, " + currentUser.getName());

            // Direct the user to the action menu
            handleUserActions();
        } catch (IOException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
    }

    /**
     * Displays the main user menu after login.
     * Users can view and manage their portfolio, cash, and transactions.
     * Calls corresponding methods for each action based on the user's input.
     */
    private static void handleUserActions() {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View Cash Reserves");
            System.out.println("2. Deposit Cash");
            System.out.println("3. Withdraw Cash");
            System.out.println("4. Buy Cryptocurrency");
            System.out.println("5. Sell Cryptocurrency");
            System.out.println("6. View Portfolio Value");
            System.out.println("7. View All Transactions");
            System.out.println("8. View Detailed Portfolio");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine()); // Read user's choice

            try {
                // Handle each action
                switch (choice) {
                    case 1:
                        viewCashReserves();
                        break;
                    case 2:
                        depositCash();
                        break;
                    case 3:
                        withdrawCash();
                        break;
                    case 4:
                        buyCrypto();
                        break;
                    case 5:
                        sellCrypto();
                        break;
                    case 6:
                        viewPortfolioValue();
                        break;
                    case 7:
                        viewAllTransactions();
                        break;
                    case 8:
                        viewDetailedPortfolio();
                        break;
                    case 9:
                        System.out.println("Logging out...");
                        return; // Exit the user menu
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the user's cash reserves.
     * Uses `User.getCashBalance` to fetch the latest balance from Firebase.
     *
     * Example output:
     * System: Your current cash reserves: $500.00
     */
    private static void viewCashReserves() throws IOException {
        double cashBalance = currentUser.getCashBalance();
        System.out.printf("Your current cash reserves: $%.2f%n", cashBalance);
    }

    /**
     * Allows the user to deposit cash into their account.
     * Uses `User.depositCash`, which updates Firebase via `FireBaseAPIClient.addCash`.
     *
     * Example interaction:
     * User: Enter the amount to deposit: 100
     * System: Deposit successful!
     */
    private static void depositCash() throws IOException {
        System.out.print("Enter the amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine()); // Read deposit amount

        currentUser.depositCash(amount);
        System.out.println("Deposit successful!");
    }

    /**
     * Allows the user to withdraw cash from their account.
     * Uses `User.withdrawCash`, which updates Firebase via `FireBaseAPIClient.withdrawCash`.
     *
     * Example interaction:
     * User: Enter the amount to withdraw: 50
     * System: Withdrawal successful!
     */
    private static void withdrawCash() throws IOException {
        System.out.print("Enter the amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine()); // Read withdrawal amount

        currentUser.withdrawCash(amount);
        System.out.println("Withdrawal successful!");
    }

    /**
     * Allows the user to buy cryptocurrency.
     * Uses `User.buyCrypto`, which:
     * - Fetches the cryptocurrency price using `BlockChainAPIClient`.
     * - Updates Firebase via `FireBaseAPIClient`.
     *
     * Example interaction:
     * User: Enter the cryptocurrency name (e.g., BTC): BTC
     * User: Enter the amount to buy: 0.01
     * System: Purchase successful!
     */
    private static void buyCrypto() throws Exception {
        System.out.print("Enter the cryptocurrency name (e.g., BTC): ");
        String cryptoName = scanner.nextLine(); // Get the crypto name
        System.out.print("Enter the amount to buy: ");
        double amount = Double.parseDouble(scanner.nextLine()); // Get the amount to buy

        currentUser.buyCrypto(cryptoName, amount);
        System.out.println("Purchase successful!");
    }

    /**
     * Allows the user to sell cryptocurrency.
     * Uses `User.sellCrypto`, which:
     * - Fetches the cryptocurrency price using `BlockChainAPIClient`.
     * - Updates Firebase via `FireBaseAPIClient`.
     *
     * Example interaction:
     * User: Enter the cryptocurrency name (e.g., BTC): BTC
     * User: Enter the amount to sell: 0.01
     * System: Sale successful!
     */
    private static void sellCrypto() throws Exception {
        System.out.print("Enter the cryptocurrency name (e.g., BTC): ");
        String cryptoName = scanner.nextLine(); // Get the crypto name
        System.out.print("Enter the amount to sell: ");
        double amount = Double.parseDouble(scanner.nextLine()); // Get the amount to sell

        currentUser.sellCrypto(cryptoName, amount);
        System.out.println("Sale successful!");
    }

    /**
     * Displays the user's total portfolio value (cash + holdings).
     * Uses `User.calculatePortfolioValue`, which:
     * - Fetches cryptocurrency prices using `BlockChainAPIClient`.
     *
     * Example output:
     * System: Your total portfolio value (cash + holdings): $1000.00
     */
    private static void viewPortfolioValue() throws Exception {
        double totalValue = currentUser.calculatePortfolioValue();
        System.out.printf("Your total portfolio value (cash + holdings): $%.2f%n", totalValue);
    }

    /**
     * Displays the user's transaction history.
     * Uses `User.getTransactions` to fetch data from Firebase via `FireBaseAPIClient`.
     *
     * Example output:
     * System: Your Transactions:
     *         Transaction{id=1, cryptoName='BTC', amount=0.01, price=50000.0, date=...}
     */
    private static void viewAllTransactions() throws IOException {
        System.out.println("\nYour Transactions:");
        List<Transaction> transactions = currentUser.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    /**
     * Displays detailed information about the user's portfolio.
     * Uses `User.getPortfolio` to fetch data from Firebase via `FireBaseAPIClient`.
     *
     * Example output:
     * System: Your Portfolio:
     *         Cryptocurrency: BTC, Amount: 0.010000
     *         Cryptocurrency: ETH, Amount: 0.500000
     */
    private static void viewDetailedPortfolio() throws IOException {
        System.out.println("\nYour Portfolio:");
        List<PortfolioEntry> portfolio = currentUser.getPortfolio();

        if (portfolio.isEmpty()) {
            System.out.println("No cryptocurrency holdings found.");
        } else {
            for (PortfolioEntry entry : portfolio) {
                System.out.printf("Cryptocurrency: %s, Amount: %.6f%n", entry.getCryptoName(), entry.getAmount());
            }
        }
    }
}
