package views;

import api.BlockChainAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;
import interface_adapterss.ViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AfterLoginView extends JPanel {

    private final User currentUser;
    private final JFrame frame;
    private final ViewModel viewmodel;

    private final JButton refreshButton = new JButton("Refresh");
    private final JLabel cashReservesLabel = new JLabel("Cash Reserves: $0.00");
    private final JLabel totalPortfolioValueLabel = new JLabel("Total Portfolio Value: $0.00");
    private final JPanel portfolioPanel = new JPanel();
    private final JPanel transactionsPanel = new JPanel();
    private final JPanel cryptoPricesPanel = new JPanel();
    private final Timer priceUpdateTimer = new Timer();

    public AfterLoginView(ViewModel viewmodel, JFrame frame) {
        this.viewmodel = viewmodel;
        this.frame = frame;
        this.currentUser = viewmodel.getCurrentUser();

        // Main layout
        setLayout(new BorderLayout());

        // Header section
        JPanel headerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        headerPanel.add(cashReservesLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        headerPanel.add(totalPortfolioValueLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        refreshButton.addActionListener(e -> updatePortfolioValue());
        headerPanel.add(refreshButton, gbc);

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        portfolioPanel.setLayout(new BoxLayout(portfolioPanel, BoxLayout.Y_AXIS));
        JScrollPane portfolioScrollPane = new JScrollPane(portfolioPanel);
        tabbedPane.addTab("Portfolio", portfolioScrollPane);

        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        JScrollPane transactionsScrollPane = new JScrollPane(transactionsPanel);
        tabbedPane.addTab("Transactions", transactionsScrollPane);

        cryptoPricesPanel.setLayout(new BoxLayout(cryptoPricesPanel, BoxLayout.Y_AXIS));
        JScrollPane pricesScrollPane = new JScrollPane(cryptoPricesPanel);
        tabbedPane.addTab("Crypto Prices", pricesScrollPane);

        // Footer section
        JPanel footerPanel = new JPanel(new FlowLayout());
        JButton depositButton = new JButton("Deposit Cash");
        JButton buyCryptoButton = new JButton("Buy Crypto");
        JButton sellCryptoButton = new JButton("Sell Crypto");
        JButton logout = new JButton("Logout");

        depositButton.addActionListener(e -> handleDeposit());
        buyCryptoButton.addActionListener(e -> handleBuyCrypto());
        sellCryptoButton.addActionListener(e -> handleSellCrypto());
        logout.addActionListener(e -> {
            this.viewmodel.setCurrentState(ViewModel.LOGIN_VIEW);
        });

        footerPanel.add(depositButton);
        footerPanel.add(buyCryptoButton);
        footerPanel.add(sellCryptoButton);
        footerPanel.add(logout);

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Initial data load
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
        priceUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadCryptoPrices();
            }
        }, 0, 3000); // Update every 30 seconds
    }

    public void dispose() {
        if (priceUpdateTimer != null) {
            priceUpdateTimer.cancel();
        }
        frame.dispose();
    }
}