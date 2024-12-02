package experimental;

import data_access.BlockChainApiClient;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;
import interactors.UserService;
import news_search.NewsSearchDataAccessInterface;
import news_search.NewsSearchInteractor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * A revamped GUI demo for the Crypto Portfolio Management System.
 * Follows SOLID principles by decoupling GUI logic from business operations.
 */
public class GUIDemo extends JFrame {

    private NewsSearchDataAccessInterface newsService; // Depend on the interface
    private UserService userService;
    private User currentUser;

    private JLabel cashReservesLabel;
    private JLabel totalPortfolioValueLabel;
    private JPanel portfolioPanel;
    private JPanel transactionsPanel;
    private JPanel newsSearchPanel = new JPanel();
    private final JTextField newsSearchField = new JTextField(30);
    private final JTextArea newsResultsArea = new JTextArea();    private JPanel cryptoPricesPanel;
    private JLabel usernameLabel;
    private JLabel emailLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIDemo frame = new GUIDemo();
            frame.setVisible(true);
        });
    }

    public GUIDemo() {
        // Initialize the user service with the adapters
        this.userService = new UserService(
                new interface_adapters.FirebaseServiceAdapter(), // Pass the FirebaseServiceAdapter
                new interface_adapters.BlockchainServiceAdapter() // Pass the BlockchainServiceAdapter
        );
        this.newsService = new NewsSearchInteractor();

        // Frame setup
        setTitle("NovaStar Cryptocurrency Trading Platform\n");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        showLoginScreen();
    }


    // Method to search for news articles based on the user's query
    private void searchNews() {
        String searchQuery = newsSearchField.getText().trim();
        if (searchQuery.isEmpty()) {
            newsResultsArea.setText("Please enter a search query.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            newsResultsArea.setText("Loading...");
            try {
                List<String> articles = newsService.fetchNews(searchQuery); // Use INewsService
                if (articles.isEmpty()) {
                    newsResultsArea.setText("No news articles found.");
                } else {
                    StringBuilder resultsText = new StringBuilder();
                    for (String article : articles) {
                        resultsText.append(article).append("\n");
                    }
                    newsResultsArea.setText(resultsText.toString());
                }
            } catch (Exception e) {
                newsResultsArea.setText("Error fetching news.");
            }
        });
    }


    private void showLoginScreen() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(new Color(245, 245, 245));
        loginPanel.setBorder(new EmptyBorder(50, 100, 50, 100));

        JLabel titleLabel = new JLabel("Crypto Portfolio Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(33, 150, 243));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setMaximumSize(new Dimension(400, 40));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setMaximumSize(new Dimension(400, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = createStyledButton("Login", new Color(33, 150, 243));
        JButton registerButton = createStyledButton("Register", new Color(76, 175, 80));
        JButton resetPasswordButton = createStyledButton("Forgot Password?", new Color(244, 67, 54));

        loginButton.addActionListener(e -> handleLogin(emailField.getText(), new String(passwordField.getPassword()), statusLabel));
        registerButton.addActionListener(e -> handleRegister(emailField.getText(), new String(passwordField.getPassword()), statusLabel));
        resetPasswordButton.addActionListener(e -> handlePasswordReset(emailField.getText(), statusLabel));

        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createVerticalStrut(40));
        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(registerButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(resetPasswordButton);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(statusLabel);

        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    private void showMainScreen() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel appTitle = new JLabel("Crypto Portfolio Manager");
        appTitle.setFont(new Font("Arial", Font.BOLD, 26));
        appTitle.setForeground(Color.WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameLabel = new JLabel("Username: " + currentUser.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailLabel = new JLabel("Email: " + currentUser.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton editProfileButton = createStyledButton("Edit Profile", Color.WHITE);
        editProfileButton.addActionListener(e -> handleProfileUpdate());

        JButton logoutButton = createStyledButton("Logout", new Color(244, 67, 54));
        logoutButton.addActionListener(e -> handleLogout());

        headerPanel.add(appTitle);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(usernameLabel);
        headerPanel.add(emailLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(logoutButton);


        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane for portfolio, transactions, and crypto prices
        JTabbedPane tabbedPane = new JTabbedPane();

        portfolioPanel = new JPanel();
        transactionsPanel = new JPanel();
        cryptoPricesPanel = new JPanel();
        newsSearchPanel = new JPanel();
        // Header Panel with cash reserves and portfolio value
        cashReservesLabel = new JLabel("Cash Reserves: Loading...");
        cashReservesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        cashReservesLabel.setForeground(Color.WHITE);

        totalPortfolioValueLabel = new JLabel("Total Portfolio Value: Loading...");
        totalPortfolioValueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPortfolioValueLabel.setForeground(Color.WHITE);

        headerPanel.add(cashReservesLabel);
        headerPanel.add(totalPortfolioValueLabel);


        tabbedPane.addTab("Portfolio", new JScrollPane(portfolioPanel));
        tabbedPane.addTab("Transactions", new JScrollPane(transactionsPanel));
        tabbedPane.addTab("Crypto Prices", new JScrollPane(cryptoPricesPanel));

        newsSearchPanel.setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search News:"));
        searchPanel.add(newsSearchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchNews());
        searchPanel.add(searchButton);

        newsResultsArea.setEditable(false);
        newsSearchPanel.add(searchPanel, BorderLayout.NORTH);
        newsSearchPanel.add(new JScrollPane(newsResultsArea), BorderLayout.CENTER);
        tabbedPane.addTab("News Search", newsSearchPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(245, 245, 245));

        JButton depositButton = createStyledButton("Deposit", new Color(76, 175, 80));
        depositButton.addActionListener(e -> handleDeposit());

        JButton withdrawButton = createStyledButton("Withdraw", new Color(244, 67, 54));
        withdrawButton.addActionListener(e -> handleWithdraw());

        footerPanel.add(depositButton);
        footerPanel.add(withdrawButton);
        addBuyByCodeFeature(footerPanel);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        revalidate();
        repaint();

        // Load data into tabs
        updatePortfolioValue();
        loadPortfolio();
        loadTransactions();
        loadCryptoPrices();
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Increased font size and weight
        button.setForeground(new Color(0, 157, 255)); // Text color
        button.setBackground(new Color(255, 255, 255)); // Background color
        button.setFocusPainted(false); // Removes focus border
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker()); // Slightly darker on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor); // Reset to original color
            }
        });

        return button;
    }

    private void handleLogin(String email, String password, JLabel statusLabel) {
        try {
            this.currentUser = userService.loginUser(email, password);
            showMainScreen();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handleRegister(String email, String password, JLabel statusLabel) {
        try {
            this.currentUser = userService.registerUser("tester", email, password, "1+1=", "2");
            showMainScreen();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handlePasswordReset(String email, JLabel statusLabel) {
        try {
            userService.resetPassword(email);
            statusLabel.setText("Password reset email sent successfully!");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handleProfileUpdate() {
        JTextField usernameField = new JTextField(currentUser.getUsername());
        JTextField emailField = new JTextField(currentUser.getEmail());

        int option = JOptionPane.showConfirmDialog(
                this,
                new Object[]{"Update Username:", usernameField, "Update Email:", emailField},
                "Edit Profile",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                String newUsername = usernameField.getText();
                String newEmail = emailField.getText();
                userService.updateUserProfile(currentUser, newUsername, newEmail);
                usernameLabel.setText("Username: " + currentUser.getUsername());
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: Unable to update profile.");
            }
        }
    }

    private void handleLogout() {
        currentUser = null;
        showLoginScreen();
    }

    private void updatePortfolioValue() {
        SwingUtilities.invokeLater(() -> {
            try {
                double cashBalance = currentUser.getCashBalance(); // Get cash reserves
                double portfolioValue = userService.calculatePortfolioValue(currentUser); // Get total value of holdings
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
            portfolioPanel.setLayout(new BoxLayout(portfolioPanel, BoxLayout.Y_AXIS));
            portfolioPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            try {
                List<PortfolioEntry> portfolio = currentUser.getPortfolio();
                if (portfolio.isEmpty()) {
                    portfolioPanel.add(new JLabel("No cryptocurrency holdings found."));
                } else {
                    for (PortfolioEntry entry : portfolio) {
                        JPanel portfolioCard = new JPanel(new BorderLayout());
                        portfolioCard.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));
                        portfolioCard.setBackground(Color.WHITE);
                        portfolioCard.setMaximumSize(new Dimension(800, 80));

                        // Cryptocurrency details
                        JLabel nameLabel = new JLabel(entry.getCryptoName());
                        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
                        nameLabel.setForeground(new Color(33, 150, 243));

                        double currentValue = userService.getCurrentValueOfHolding(entry);
                        double growth = currentValue - userService.getInitialInvestment(currentUser, entry);


                        JLabel valueLabel = new JLabel(String.format("Value: $%.2f (Growth: %.2f%%)", currentValue, (growth / userService.getInitialInvestment(currentUser,entry)) * 100));
                        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                        valueLabel.setForeground(growth >= 0 ? new Color(76, 175, 80) : new Color(244, 67, 54));

                        portfolioCard.add(nameLabel, BorderLayout.WEST);
                        portfolioCard.add(valueLabel, BorderLayout.EAST);

                        portfolioPanel.add(portfolioCard);
                        portfolioPanel.add(Box.createVerticalStrut(10));
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
            transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
            transactionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            try {
                List<Transaction> transactions = currentUser.getTransactions();
                if (transactions.isEmpty()) {
                    transactionsPanel.add(new JLabel("No transactions found."));
                } else {
                    for (Transaction transaction : transactions) {
                        JPanel transactionCard = new JPanel(new BorderLayout());
                        transactionCard.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));
                        transactionCard.setBackground(Color.WHITE);
                        transactionCard.setMaximumSize(new Dimension(800, 60));

                        JLabel detailsLabel = new JLabel(String.format(
                                "%s %s: %.6f units at $%.2f each on %s",
                                transaction.getType(),
                                transaction.getCryptoName(),
                                transaction.getAmount(),
                                transaction.getPrice(),
                                transaction.getDate()
                        ));
                        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                        detailsLabel.setForeground(Color.BLACK);

                        transactionCard.add(detailsLabel, BorderLayout.CENTER);
                        transactionsPanel.add(transactionCard);
                        transactionsPanel.add(Box.createVerticalStrut(10));
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
            cryptoPricesPanel.setLayout(new BoxLayout(cryptoPricesPanel, BoxLayout.Y_AXIS)); // Vertical layout
            cryptoPricesPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around the edges

            try {
                List<BlockChainApiClient.CryptoInfo> cryptos = BlockChainApiClient.fetchPopularCryptos();
                for (BlockChainApiClient.CryptoInfo crypto : cryptos) {
                    // Create a panel for each cryptocurrency
                    JPanel cryptoCard = new JPanel(new BorderLayout());
                    cryptoCard.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(200, 200, 200)), // Border around the card
                            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Inner padding
                    ));
                    cryptoCard.setBackground(Color.WHITE);
                    cryptoCard.setMaximumSize(new Dimension(800, 80)); // Limit card size
                    cryptoCard.setPreferredSize(new Dimension(800, 80));

                    // Cryptocurrency name and symbol
                    JLabel nameLabel = new JLabel(crypto.getName() + " (" + crypto.getSymbol() + ")");
                    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    nameLabel.setForeground(new Color(33, 150, 243));

                    // Price
                    JLabel priceLabel = new JLabel(String.format("$%.2f", crypto.getCurrentPrice()));
                    priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    priceLabel.setForeground(Color.BLACK);

                    // Percentage change with color
                    JLabel changeLabel = new JLabel(String.format("%.2f%%", crypto.getPercentageChange()));
                    changeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    if (crypto.getPercentageChange() > 0) {
                        changeLabel.setForeground(new Color(76, 175, 80)); // Green for positive change
                    } else {
                        changeLabel.setForeground(new Color(244, 67, 54)); // Red for negative change
                    }

                    // Add components to the card
                    JPanel textPanel = new JPanel(new GridLayout(2, 1));
                    textPanel.setBackground(Color.WHITE);
                    textPanel.add(nameLabel);
                    textPanel.add(priceLabel);

                    cryptoCard.add(textPanel, BorderLayout.CENTER);
                    cryptoCard.add(changeLabel, BorderLayout.EAST);

                    // Add click listener to buy/sell
                    cryptoCard.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            handleCryptoAction(crypto); // Open buy/sell dialog for this cryptocurrency
                        }
                    });

                    // Add the card to the main panel
                    cryptoPricesPanel.add(cryptoCard);
                    cryptoPricesPanel.add(Box.createVerticalStrut(10)); // Add space between cards
                }
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Error fetching cryptocurrency prices.");
                errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
                errorLabel.setForeground(Color.RED);
                cryptoPricesPanel.add(errorLabel);
            }

            cryptoPricesPanel.revalidate();
            cryptoPricesPanel.repaint();
        });
    }

    private void handleCryptoAction(BlockChainApiClient.CryptoInfo crypto) {
        String[] options = {"Buy", "Sell", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "What would you like to do with " + crypto.getName() + "?",
                "Buy or Sell " + crypto.getName(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) { // Buy
            String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to buy:");
            if (amountStr != null) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    userService.buyCrypto(currentUser, crypto.getName(), amount);
                    updatePortfolioValue();
                    loadPortfolio();
                    loadTransactions();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: Unable to buy cryptocurrency.");
                }
            }
        } else if (choice == 1) { // Sell
            String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to sell:");
            if (amountStr != null) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    userService.sellCrypto(currentUser, crypto.getName(), amount);
                    updatePortfolioValue();
                    loadPortfolio();
                    loadTransactions();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: Unable to sell cryptocurrency.");
                }
            }
        }
    }

    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to deposit:");
        if (amountStr != null) { // Check if the user canceled the input dialog
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                userService.depositCash(currentUser, amount); // Call the UserService to update the balance
                JOptionPane.showMessageDialog(this, String.format("Successfully deposited $%.2f", amount));
                updatePortfolioValue(); // Refresh cash reserves and portfolio value display
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: Unable to deposit cash. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleWithdraw() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to withdraw:");
        if (amountStr != null) { // Check if the user canceled the input dialog
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (amount > currentUser.getCashBalance()) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds for this withdrawal.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                userService.withdrawCash(currentUser, amount); // Call the UserService to update the balance
                JOptionPane.showMessageDialog(this, String.format("Successfully withdrew $%.2f", amount));
                updatePortfolioValue(); // Refresh cash reserves and portfolio value display
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: Unable to withdraw cash. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addBuyByCodeFeature(JPanel footerPanel) {
        JButton buyByCodeButton = createStyledButton("Buy by Code", new Color(0, 91, 163));
        buyByCodeButton.addActionListener(e -> handleBuyByCode());
        footerPanel.add(buyByCodeButton);
    }

    private void handleBuyByCode() {
        // Prompt the user to input the cryptocurrency code and amount
        String cryptoCode = JOptionPane.showInputDialog(this, "Enter the cryptocurrency code (e.g., BTC):");
        if (cryptoCode != null && !cryptoCode.trim().isEmpty()) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter the amount to buy:");
            if (amountStr != null && !amountStr.trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Attempt to buy the cryptocurrency
                    userService.buyCrypto(currentUser, cryptoCode, amount);
                    JOptionPane.showMessageDialog(this, String.format("Successfully bought %.6f units of %s", amount, cryptoCode));
                    updatePortfolioValue();
                    loadPortfolio();
                    loadTransactions();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: Unable to buy cryptocurrency. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
// Method to search for news articles based on the user's query



    }






}
