package controllers;

import api.BlockChainAPIClient;
import api.BlockChainAPIClient.CryptoInfo;
import entities.User;
import services.UserService;
import views.CryptoPricesView;

import javax.swing.*;
import java.util.List;

public class CryptoPricesController {

    private final UserService userService;
    private final User currentUser;
    private final CryptoPricesView cryptoPricesView;

    private final PortfolioController portfolioController;
    private final TransactionsController transactionsController;

    public CryptoPricesController(UserService userService, User currentUser, CryptoPricesView cryptoPricesView,
                                  PortfolioController portfolioController, TransactionsController transactionsController) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.cryptoPricesView = cryptoPricesView;
        this.portfolioController = portfolioController;
        this.transactionsController = transactionsController;
    }

    public JPanel getView() {
        loadCryptoPrices();
        return cryptoPricesView;
    }

    private void loadCryptoPrices() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<CryptoInfo> cryptoPrices = BlockChainAPIClient.fetchPopularCryptos();
                cryptoPricesView.updateCryptoPrices(cryptoPrices, this::handleCryptoAction);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(cryptoPricesView, "Error loading cryptocurrency prices: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void handleCryptoAction(CryptoInfo crypto) {
        String[] options = {"Buy", "Sell", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                cryptoPricesView,
                "What would you like to do with " + crypto.getName() + "?",
                "Buy or Sell " + crypto.getName(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) { // Buy
            handleBuy(crypto);
        } else if (choice == 1) { // Sell
            handleSell(crypto);
        }
    }

    private void handleBuy(CryptoInfo crypto) {
        String amountStr = JOptionPane.showInputDialog(cryptoPricesView, "Enter the amount to buy:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                userService.buyCrypto(currentUser, crypto.getName(), amount);
                JOptionPane.showMessageDialog(cryptoPricesView, "Successfully bought " + amount + " units of " + crypto.getName());
                refreshData(); // Refresh portfolio and transactions
            } catch (Exception e) {
                JOptionPane.showMessageDialog(cryptoPricesView, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSell(CryptoInfo crypto) {
        String amountStr = JOptionPane.showInputDialog(cryptoPricesView, "Enter the amount to sell:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                userService.sellCrypto(currentUser, crypto.getName(), amount);
                JOptionPane.showMessageDialog(cryptoPricesView, "Successfully sold " + amount + " units of " + crypto.getName());
                refreshData(); // Refresh portfolio and transactions
            } catch (Exception e) {
                JOptionPane.showMessageDialog(cryptoPricesView, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData() {
        try {
            portfolioController.refresh();
            transactionsController.refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(cryptoPricesView, "Error refreshing data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
