package application.controllers;

import infrastructure.api_clients.BlockChainApiClient.CryptoInfo;
import domain.entities.User;
import application.interactors.buy_crypto.BuyCryptoInteractor;
import application.interactors.fetch_crypto_prices.FetchCryptoPricesInteractor;
import application.interactors.sell_crypto.SellCryptoInteractor;
import views.CryptoPricesView;
import views.MainView;

import javax.swing.*;
import java.util.List;

public class CryptoPricesController {

    public static final String ERROR = "Error";
    public static final String ERROR_LOAD_CRYP_PRICE = "Error loading cryptocurrency prices: ";

    private final FetchCryptoPricesInteractor fetchCryptoPricesInteractor;
    private final BuyCryptoInteractor buyCryptoInteractor;
    private final SellCryptoInteractor sellCryptoInteractor;
    private final CryptoPricesView cryptoPricesView;
    private final PortfolioController portfolioController;
    private final TransactionsController transactionsController;
    private final MainController mainController;
    private final User currentUser;
    private MainView mainView;

    /**
     * Constructor for CryptoPricesController.
     *
     * @param fetchCryptoPricesInteractor Interactor to fetch crypto prices.
     * @param buyCryptoInteractor         Interactor to handle buying crypto.
     * @param sellCryptoInteractor        Interactor to handle selling crypto.
     * @param currentUser                 The currently logged-in user.
     * @param cryptoPricesView            View for displaying crypto prices.
     * @param portfolioController         Controller for the portfolio tab.
     * @param transactionsController      Controller for the transactions tab.
     * @param mainView
     */
    public CryptoPricesController(FetchCryptoPricesInteractor fetchCryptoPricesInteractor,
                                  BuyCryptoInteractor buyCryptoInteractor,
                                  SellCryptoInteractor sellCryptoInteractor,
                                  User currentUser,
                                  CryptoPricesView cryptoPricesView,
                                  PortfolioController portfolioController,
                                  TransactionsController transactionsController, MainController mainController, MainView mainView) {
        this.fetchCryptoPricesInteractor = fetchCryptoPricesInteractor;
        this.buyCryptoInteractor = buyCryptoInteractor;
        this.sellCryptoInteractor = sellCryptoInteractor;
        this.currentUser = currentUser;
        this.cryptoPricesView = cryptoPricesView;
        this.portfolioController = portfolioController;
        this.transactionsController = transactionsController;
        this.mainController = mainController;
    }

    /**
     * Returns the crypto prices view as a JPanel.
     *
     * @return JPanel representing the crypto prices view.
     */
    public JPanel getView() {
        loadCryptoPrices();
        return cryptoPricesView;
    }

    /**
     * Loads cryptocurrency prices and updates the view.
     */
    private void loadCryptoPrices() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<CryptoInfo> cryptoPrices = fetchCryptoPricesInteractor.execute();
                cryptoPricesView.updateCryptoPrices(cryptoPrices, this::handleCryptoAction);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(cryptoPricesView,
                        ERROR_LOAD_CRYP_PRICE + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Handles user actions for a selected cryptocurrency (Buy/Sell).
     *
     * @param crypto The selected cryptocurrency.
     */
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
        } else if (choice == 1) {
            handleSell(crypto);
        }
    }

    /**
     * Handles buying cryptocurrency.
     *
     * @param crypto The cryptocurrency to buy.
     */
    private void handleBuy(CryptoInfo crypto) {
        final String amountStr = JOptionPane.showInputDialog(cryptoPricesView, "Enter the amount to buy:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                buyCryptoInteractor.execute(currentUser, crypto.getName(), amount);
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Successfully bought " + amount + " units of " + crypto.getName());
                refreshData();
                portfolioController.refresh();
                transactionsController.refresh();
                mainController.updateHeaderValues(mainController.getMainView());
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Error: " + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles selling cryptocurrency.
     *
     * @param crypto The cryptocurrency to sell.
     */
    private void handleSell(CryptoInfo crypto) {
        final String amountStr = JOptionPane.showInputDialog(cryptoPricesView, "Enter the amount to sell:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                sellCryptoInteractor.execute(currentUser, crypto.getName(), amount);
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Successfully sold " + amount + " units of " + crypto.getName());
                refreshData();
                portfolioController.refresh();
                transactionsController.refresh();
                mainController.updateHeaderValues(mainController.getMainView());
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(cryptoPricesView,
                        "Error: " + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Refreshes the portfolio and transactions views.
     */
    private void refreshData() {
        try {
            portfolioController.refresh();
            transactionsController.refresh();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(cryptoPricesView, "Error refreshing data: " + exception.getMessage(),
                    ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
