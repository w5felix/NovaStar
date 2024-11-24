import api.FireBaseAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CommandLineDemo {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        System.out.println("Welcome to the Crypto Portfolio Management System");
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    handleRegister();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleRegister() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            String userId = FireBaseAPIClient.registerUser(email, password);
            System.out.println("Registration successful! Your User ID: " + userId);
        } catch (IOException e) {
            System.err.println("Error during registration: " + e.getMessage());
        }
    }

    private static void handleLogin() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            String userId = FireBaseAPIClient.loginUser(email, password);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            currentUser = new User(userId, name);
            System.out.println("Login successful! Welcome, " + currentUser.getName());

            handleUserActions();
        } catch (IOException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
    }

    private static void handleUserActions() {
        while (true) {
            System.out.println("\n1. View Cash Reserves");
            System.out.println("2. Deposit Cash");
            System.out.println("3. Withdraw Cash");
            System.out.println("4. Buy Cryptocurrency");
            System.out.println("5. Sell Cryptocurrency");
            System.out.println("6. View Portfolio Value");
            System.out.println("7. View All Transactions");
            System.out.println("8. View Detailed Portfolio");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            try {
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
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void viewCashReserves() throws IOException {
        double cashBalance = currentUser.getCashBalance();
        System.out.printf("Your current cash reserves: $%.2f%n", cashBalance);
    }

    private static void depositCash() throws IOException {
        System.out.print("Enter the amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());

        currentUser.depositCash(amount);
        System.out.println("Deposit successful!");
    }

    private static void withdrawCash() throws IOException {
        System.out.print("Enter the amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());

        currentUser.withdrawCash(amount);
        System.out.println("Withdrawal successful!");
    }

    private static void buyCrypto() throws Exception {
        System.out.print("Enter the cryptocurrency name (e.g., BTC): ");
        String cryptoName = scanner.nextLine();
        System.out.print("Enter the amount to buy: ");
        double amount = Double.parseDouble(scanner.nextLine());

        currentUser.buyCrypto(cryptoName, amount);
        System.out.println("Purchase successful!");
    }

    private static void sellCrypto() throws Exception {
        System.out.print("Enter the cryptocurrency name (e.g., BTC): ");
        String cryptoName = scanner.nextLine();
        System.out.print("Enter the amount to sell: ");
        double amount = Double.parseDouble(scanner.nextLine());

        currentUser.sellCrypto(cryptoName, amount);
        System.out.println("Sale successful!");
    }

    private static void viewPortfolioValue() throws Exception {
        double totalValue = currentUser.calculatePortfolioValue();
        System.out.printf("Your total portfolio value (cash + holdings): $%.2f%n", totalValue);
    }

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
