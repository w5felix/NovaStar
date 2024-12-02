/*
import api.BlockChainAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

*/
/**
 * A GUI demo for the Crypto Portfolio Management System.
 * Allows users to log in, manage their portfolio, and view real-time updates of portfolio values.
 *//*

public class GUIDemo extends JFrame {

    private User currentUser;
    private JLabel cashReservesLabel;
    private JLabel totalPortfolioValueLabel;
    private JPanel portfolioPanel;
    private JPanel transactionsPanel;
    private JPanel cryptoPricesPanel;
    private Timer priceUpdateTimer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIDemo frame = new GUIDemo();
            frame.setVisible(true);
        });
    }

    public GUIDemo() {
        setTitle("Crypto Portfolio Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        showLoginScreen();
    }

    private void showLoginScreen() {
        JPanel loginPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        loginPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> handleLogin(emailField.getText(), new String(passwordField.getPassword()), statusLabel));
        registerButton.addActionListener(e -> handleRegister(emailField.getText(), new String(passwordField.getPassword()), statusLabel));

        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        loginPanel.add(statusLabel);

        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    private void handleRegister(String email, String password, JLabel statusLabel) {
        try {
            this.currentUser = new User("tester", email, password, "1+1=", "2");
            showMainScreen();

        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handleLogin(String email, String password, JLabel statusLabel) {
        try {
            this.currentUser = new User(email, password);
            showMainScreen();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void showMainScreen() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header Panel with cash reserves and portfolio value
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        cashReservesLabel = new JLabel("Cash Reserves: Loading...");
        totalPortfolioValueLabel = new JLabel("Total Portfolio Value: Loading...");
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> updatePortfolioValue());
        headerPanel.add(cashReservesLabel);
        headerPanel.add(totalPortfolioValueLabel);
        headerPanel.add(refreshButton);

        // Center Panel with tabs for portfolio, transactions, and crypto prices
        JTabbedPane tabbedPane = new JTabbedPane();

        // Portfolio Tab
        portfolioPanel = new JPanel();
        portfolioPanel.setLayout(new BoxLayout(portfolioPanel, BoxLayout.Y_AXIS));
        JScrollPane portfolioScrollPane = new JScrollPane(portfolioPanel);
        tabbedPane.addTab("Portfolio", portfolioScrollPane);

        // Transactions Tab
        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        JScrollPane transactionsScrollPane = new JScrollPane(transactionsPanel);
        tabbedPane.addTab("Transactions", transactionsScrollPane);

        // Crypto Prices Tab
        cryptoPricesPanel = new JPanel();
        cryptoPricesPanel.setLayout(new BoxLayout(cryptoPricesPanel, BoxLayout.Y_AXIS));
        JScrollPane pricesScrollPane = new JScrollPane(cryptoPricesPanel);
        tabbedPane.addTab("Crypto Prices", pricesScrollPane);

        // Footer Panel with actions
        JPanel footerPanel = new JPanel(new FlowLayout());
        JButton depositButton = new JButton("Deposit Cash");
        JButton buyCryptoButton = new JButton("Buy Crypto");
        JButton sellCryptoButton = new JButton("Sell Crypto");

        depositButton.addActionListener(e -> handleDeposit());
        buyCryptoButton.addActionListener(e -> handleBuyCrypto());
        sellCryptoButton.addActionListener(e -> handleSellCrypto());

        footerPanel.add(depositButton);
        footerPanel.add(buyCryptoButton);
        footerPanel.add(sellCryptoButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        revalidate();
        repaint();

        // Load data
        updatePortfolioValue();
        loadPortfolio();
        loadTransactions();
        loadCryptoPrices();
        startPriceUpdates();
    }

    private void updatePortfolioValue() {
        SwingUtilities.invokeLater(() -> {
            try {
                double cashBalance = currentUser.getCashBalance();
                double portfolioValue = currentUser.calculatePortfolioValue();
                cashReservesLabel.setText(String.format("Cash Reserves: $%.2f", cashBalance));
                totalPortfolioValueLabel.setText(String.format("Total Portfolio Value: $%.2f", portfolioValue));
            } catch (Exception e) {
                cashReservesLabel.setText("Error fetching cash reserves.");
                totalPortfolioValueLabel.setText("Error calculating portfolio value.");
            }
        });
    }

    private void loadPortfolio() {
        SwingUtilities.invokeLater(() -> {
            portfolioPanel.removeAll();
            try {
                List<PortfolioEntry> portfolio = currentUser.getPortfolio();
                if (portfolio.isEmpty()) {
                    portfolioPanel.add(new JLabel("No cryptocurrency holdings found."));
                } else {
                    for (PortfolioEntry entry : portfolio) {
                        String cryptoName = entry.getCryptoName();
                        double amount = entry.getAmount();
                        JLabel entryLabel = new JLabel(String.format("Cryptocurrency: %s, Amount: %.6f", cryptoName, amount));
                        portfolioPanel.add(entryLabel);
                    }
                }
            } catch (Exception e) {
                portfolioPanel.add(new JLabel("Error loading portfolio."));
            }
            portfolioPanel.revalidate();
            portfolioPanel.repaint();
        });
    }

    private void loadTransactions() {
        SwingUtilities.invokeLater(() -> {
            transactionsPanel.removeAll();
            try {
                List<Transaction> transactions = currentUser.getTransactions();
                if (transactions.isEmpty()) {
                    transactionsPanel.add(new JLabel("No transactions found."));
                } else {
                    for (Transaction transaction : transactions) {
                        transactionsPanel.add(new JLabel(transaction.toString()));
                    }
                }
            } catch (Exception e) {
                transactionsPanel.add(new JLabel("Error loading transactions."));
            }
            transactionsPanel.revalidate();
            transactionsPanel.repaint();
        });
    }

    private void loadCryptoPrices() {
        SwingUtilities.invokeLater(() -> {
            cryptoPricesPanel.removeAll();
            try {
                List<BlockChainAPIClient.CryptoInfo> cryptos = BlockChainAPIClient.fetchPopularCryptos();
                for (BlockChainAPIClient.CryptoInfo crypto : cryptos) {
                    JLabel cryptoLabel = new JLabel(String.format(
                            "%s (%s): $%.2f (24h: %.2f%%)",
                            crypto.getName(),
                            crypto.getSymbol(),
                            crypto.getCurrentPrice(),
                            crypto.getPercentageChange()
                    ));
                    cryptoPricesPanel.add(cryptoLabel);
                }
            } catch (Exception e) {
                cryptoPricesPanel.add(new JLabel("Error fetching cryptocurrency prices."));
            }
            cryptoPricesPanel.revalidate();
            cryptoPricesPanel.repaint();
        });
    }

    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to deposit:");
        if (amountStr != null) {
            try {
                currentUser.depositCash(Double.parseDouble(amountStr));
                updatePortfolioValue();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: Unable to deposit cash.");
            }
        }
    }

    private void handleBuyCrypto() {
        String cryptoName = JOptionPane.showInputDialog(this, "Enter the cryptocurrency name (e.g., BTC):");
        if (cryptoName != null) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to buy:");
            if (amountStr != null) {
                try {
                    currentUser.buyCrypto(cryptoName, Double.parseDouble(amountStr));
                    updatePortfolioValue();
                    loadPortfolio();
                    loadTransactions();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: Unable to buy cryptocurrency.");
                }
            }
        }
    }

    private void handleSellCrypto() {
        String cryptoName = JOptionPane.showInputDialog(this, "Enter the cryptocurrency name (e.g., BTC):");
        if (cryptoName != null) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to sell:");
            if (amountStr != null) {
                try {
                    currentUser.sellCrypto(cryptoName, Double.parseDouble(amountStr));
                    updatePortfolioValue();
                    loadPortfolio();
                    loadTransactions();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: Unable to sell cryptocurrency.");
                }
            }
        }
    }

    private void startPriceUpdates() {
        priceUpdateTimer = new Timer();
        priceUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadCryptoPrices();
            }
        }, 0, 3000); // Update every 30 seconds
    }

    @Override
    public void dispose() {
        if (priceUpdateTimer != null) {
            priceUpdateTimer.cancel();
        }
        super.dispose();
    }
}
*/
