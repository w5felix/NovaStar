package controllers;

import entities.User;
import interface_adapters.news_search.NewsSearchController;
import news_search.NewsSearchDataAccessInterface;
import interactors.UserService;
import views.*;

import javax.swing.*;

public class MainController {

    private final UserService userService;
    private final NewsSearchDataAccessInterface newsService;
    private final User currentUser;
    private final JFrame frame;

    private final PortfolioController portfolioController;
    private final TransactionsController transactionsController;

    public MainController(UserService userService, NewsSearchDataAccessInterface newsService, User currentUser, JFrame frame) {
        this.userService = userService;
        this.newsService = newsService;
        this.currentUser = currentUser;
        this.frame = frame;

        // Initialize PortfolioController
        PortfolioView portfolioView = new PortfolioView();
        this.portfolioController = new PortfolioController(userService, currentUser, portfolioView);

        // Initialize TransactionsController
        TransactionsView transactionsView = new TransactionsView();
        this.transactionsController = new TransactionsController(userService, currentUser, transactionsView);
    }

    public void start() {
        MainView mainView = new MainView();

        // Set up actions for deposit, withdraw, and buy crypto
        mainView.setDepositAction(e -> handleDeposit(mainView));
        mainView.setWithdrawAction(e -> handleWithdraw(mainView));
        mainView.setBuyCryptoAction(e -> handleBuyCrypto(mainView));

        // Set up tabs for different views
        setupTabs(mainView);

        // Update header values
        updateHeaderValues(mainView);

        frame.setContentPane(mainView);
        frame.revalidate();
    }

    private void setupTabs(MainView mainView) {
        // Portfolio tab
        mainView.addPortfolioPanel(portfolioController.getView());

        // Transactions tab
        mainView.addTransactionsPanel(transactionsController.getView());

        // Crypto Prices tab
        CryptoPricesView cryptoPricesView = new CryptoPricesView();
        CryptoPricesController cryptoPricesController = new CryptoPricesController(
                userService,
                currentUser,
                cryptoPricesView,
                portfolioController,  // Pass PortfolioController
                transactionsController // Pass TransactionsController
        );
        mainView.addCryptoPricesPanel(cryptoPricesController.getView());

        // News Search tab
        NewsSearchView newsSearchView = new NewsSearchView();
        NewsSearchController newsSearchController = new NewsSearchController(newsService, newsSearchView);
        mainView.addNewsPanel(newsSearchController.getView());
    }

    private void updateHeaderValues(MainView mainView) {
        mainView.updateCashReserves(currentUser.getCashBalance());
        try {
            mainView.updatePortfolioValue(userService.calculatePortfolioValue(currentUser));
        } catch (Exception e) {
            mainView.updatePortfolioValue(0.0);
            JOptionPane.showMessageDialog(frame, "Error calculating portfolio value: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeposit(MainView mainView) {
        String input = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }
            userService.depositCash(currentUser, amount);
            JOptionPane.showMessageDialog(frame, String.format("Successfully deposited $%.2f", amount));
            updateHeaderValues(mainView);
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWithdraw(MainView mainView) {
        String input = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }
            if (amount > currentUser.getCashBalance()) {
                throw new IllegalArgumentException("Insufficient funds.");
            }
            userService.withdrawCash(currentUser, amount);
            JOptionPane.showMessageDialog(frame, String.format("Successfully withdrew $%.2f", amount));
            updateHeaderValues(mainView);
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBuyCrypto(MainView mainView) {
        String cryptoSymbol = JOptionPane.showInputDialog(frame, "Enter cryptocurrency symbol (e.g., BTC):");
        if (cryptoSymbol == null || cryptoSymbol.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid cryptocurrency symbol.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(frame, "Enter amount to buy:");
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }
            userService.buyCrypto(currentUser, cryptoSymbol, amount);
            JOptionPane.showMessageDialog(frame, String.format("Successfully bought %.2f units of %s", amount, cryptoSymbol));
            updateHeaderValues(mainView);
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        try {
            // Refresh the portfolio and transactions data
            portfolioController.refresh();
            transactionsController.refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error refreshing data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
