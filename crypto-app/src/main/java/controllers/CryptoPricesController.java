package controllers;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import data_access.BlockChainApiClient;
import data_access.BlockChainApiClient.CryptoInfo;
import entities.User;
import interactors.UserService;
import views.CryptoPricesView;

public class CryptoPricesController {

    public static final String ERROR = "Error";
    public static final String ERROR_LOAD_CRYP_PRICE = "Error loading cryptocurrency prices: ";

    private final UserService userService;
    private final User currentUser;
    private final CryptoPricesView cryptoPricesView;

    private final PortfolioController portfolioController;
    private final TransactionsController transactionsController;

    public CryptoPricesController(UserService userService, User currentUser, CryptoPricesView cryptoPricesView,
                                  PortfolioController portfolioController,
                                  TransactionsController transactionsController) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.cryptoPricesView = cryptoPricesView;
        this.portfolioController = portfolioController;
        this.transactionsController = transactionsController;
    }

    /**
     * A JPanel to get view.
     * @return cryptoPricesView
     */
    public JPanel getView() {
        loadCryptoPrices();
        return cryptoPricesView;
    }

    private void loadCryptoPrices() {
        SwingUtilities.invokeLater(() -> {
            try {
                final List<CryptoInfo> cryptoPrices = BlockChainApiClient.fetchPopularCryptos();
                cryptoPricesView.updateCryptoPrices(cryptoPrices, this::handleCryptoAction);
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(cryptoPricesView,
                        ERROR_LOAD_CRYP_PRICE + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void handleCryptoAction(CryptoInfo crypto) {
        final String[] options = {"Buy", "Sell", "Cancel"};
        final int choice = JOptionPane.showOptionDialog(
                cryptoPricesView,
                "What would you like to do with " + crypto.getName() + "?",
                "Buy or Sell " + crypto.getName(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            handleBuy(crypto);
        }
        else if (choice == 1) {
            handleSell(crypto);
        }
    }

    private void handleBuy(CryptoInfo crypto) {
        final String amountStr = JOptionPane.showInputDialog(cryptoPricesView, "Enter the amount to buy:");
        if (amountStr != null) {
            try {
                final double amount = Double.parseDouble(amountStr);
                userService.buyCrypto(currentUser, crypto.getName(), amount);
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Successfully bought " + amount + " units of " + crypto.getName());
                refreshData();
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Error: " + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSell(CryptoInfo crypto) {
        final String amountStr = JOptionPane.showInputDialog(cryptoPricesView, "Enter the amount to sell:");
        if (amountStr != null) {
            try {
                final double amount = Double.parseDouble(amountStr);
                userService.sellCrypto(currentUser, crypto.getName(), amount);
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Successfully sold " + amount + " units of " + crypto.getName());
                refreshData();
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Error: " + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData() {
        try {
            portfolioController.refresh();
            transactionsController.refresh();
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(cryptoPricesView, "Error refreshing data: " + exception.getMessage(),
                    ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
