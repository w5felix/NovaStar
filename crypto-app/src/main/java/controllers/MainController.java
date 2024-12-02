package controllers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import entities.User;
import interactors.UserService;
import interface_adapters.news_search.NewsSearchController;
import news_search.NewsSearchDataAccessInterface;
import views.CryptoPricesView;
import views.MainView;
import views.NewsSearchView;
import views.PortfolioView;
import views.TransactionsView;

public class MainController {
    public static final String ERROR = "Error";
    public static final String AMOUNT_POSITIVE = "Amount must be positive.";
    public static final String ERROR2 = "Error: ";

    private final UserService userService;
    private final NewsSearchDataAccessInterface newsService;
    private final User currentUser;
    private final JFrame frame;

    private final PortfolioController portfolioController;
    private final TransactionsController transactionsController;

    public MainController(UserService userService,
                          NewsSearchDataAccessInterface newsService, User currentUser, JFrame frame) {
        this.userService = userService;
        this.newsService = newsService;
        this.currentUser = currentUser;
        this.frame = frame;

        // Initialize PortfolioController
        final PortfolioView portfolioView = new PortfolioView();
        this.portfolioController = new PortfolioController(userService, currentUser, portfolioView);

        // Initialize TransactionsController
        final TransactionsView transactionsView = new TransactionsView();
        this.transactionsController = new TransactionsController(userService, currentUser, transactionsView);
    }

    /**
     * Main view for starting.
     */
    public void start() {
        final MainView mainView = new MainView();

        // Set up actions for deposit, withdraw, and buy crypto
        mainView.setDepositAction(actionEvent -> handleDeposit(mainView));
        mainView.setWithdrawAction(actionEvent -> handleWithdraw(mainView));
        mainView.setBuyCryptoAction(actionEvent -> handleBuyCrypto(mainView));

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
        final CryptoPricesView cryptoPricesView = new CryptoPricesView();
        final CryptoPricesController cryptoPricesController = new CryptoPricesController(
                userService,
                currentUser,
                cryptoPricesView,
                portfolioController,
                transactionsController
        );
        mainView.addCryptoPricesPanel(cryptoPricesController.getView());

        // News Search tab
        final NewsSearchView newsSearchView = new NewsSearchView();
        final NewsSearchController newsSearchController = new NewsSearchController(newsService, newsSearchView);
        mainView.addNewsPanel(newsSearchController.getView());
    }

    private void updateHeaderValues(MainView mainView) {
        mainView.updateCashReserves(currentUser.getCashBalance());
        try {
            mainView.updatePortfolioValue(userService.calculatePortfolioValue(currentUser));
        }
        catch (Exception exception) {
            mainView.updatePortfolioValue(0.0);
            JOptionPane.showMessageDialog(frame, "Error calculating portfolio value: " + exception.getMessage(),
                    ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeposit(MainView mainView) {
        final String input = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        try {
            final double amount = Double.parseDouble(input);
            if (amount <= 0) {
                throw new IllegalArgumentException(AMOUNT_POSITIVE);
            }
            userService.depositCash(currentUser, amount);
            JOptionPane.showMessageDialog(frame, String.format("Successfully deposited $%.2f", amount));
            updateHeaderValues(mainView);
            refreshData();
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, ERROR2 + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWithdraw(MainView mainView) {
        final String input = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        try {
            final double amount = Double.parseDouble(input);
            if (amount <= 0) {
                throw new IllegalArgumentException(AMOUNT_POSITIVE);
            }
            if (amount > currentUser.getCashBalance()) {
                throw new IllegalArgumentException("Insufficient funds.");
            }
            userService.withdrawCash(currentUser, amount);
            JOptionPane.showMessageDialog(frame, String.format("Successfully withdrew $%.2f", amount));
            updateHeaderValues(mainView);
            refreshData();
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, ERROR2 + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBuyCrypto(MainView mainView) {
        final String cryptoSymbol = JOptionPane.showInputDialog(frame, "Enter cryptocurrency symbol (e.g., BTC):");
        if (cryptoSymbol == null || cryptoSymbol.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid cryptocurrency symbol.", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String input = JOptionPane.showInputDialog(frame, "Enter amount to buy:");
        try {
            final double amount = Double.parseDouble(input);
            if (amount <= 0) {
                throw new IllegalArgumentException(AMOUNT_POSITIVE);
            }
            userService.buyCrypto(currentUser, cryptoSymbol, amount);
            JOptionPane.showMessageDialog(frame,
                    String.format("Successfully bought %.2f units of %s", amount, cryptoSymbol));
            updateHeaderValues(mainView);
            refreshData();
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, "Error: " + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        try {
            // Refresh the portfolio and transactions data
            portfolioController.refresh();
            transactionsController.refresh();
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, "Error refreshing data: " + exception.getMessage(),
                    ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
