package application.controllers;

import javax.swing.*;

import application.interactors.buy_crypto.BuyCryptoInteractor;
import application.interactors.calculate_portfolio_value.CalculatePortfolioValueInteractor;
import application.interactors.deposit_cash.DepositCashInteractor;
import application.interactors.fetch_crypto_prices.FetchCryptoPricesInteractor;
import application.interactors.get_current_value.GetCurrentValueInteractor;
import application.interactors.get_initial_investment.GetInitialInvestmentInteractor;
import application.interactors.get_portfolio_entries.GetPortfolioEntriesInteractor;
import application.interactors.get_transactions.GetTransactionsInteractor;
import application.interactors.sell_crypto.SellCryptoInteractor;
import application.interactors.withdraw_cash.WithdrawCashInteractor;
import domain.entities.User;
import application.interactors.news_search.NewsSearchInteractor;
import views.*;

public class MainController {
    public static final String ERROR = "Error";
    public static final String AMOUNT_POSITIVE = "Amount must be positive.";
    public static final String ERROR2 = "Error: ";

    private MainView mainView;

    private final DepositCashInteractor depositCashInteractor;
    private final WithdrawCashInteractor withdrawCashInteractor;
    private final BuyCryptoInteractor buyCryptoInteractor;
    private final CalculatePortfolioValueInteractor calculatePortfolioValueInteractor;
    private final FetchCryptoPricesInteractor fetchCryptoPricesInteractor;
    private final BuyCryptoInteractor cryptoBuyInteractor;
    private final SellCryptoInteractor cryptoSellInteractor;
    private final NewsSearchInteractor newsSearchInteractor;
    private final GetPortfolioEntriesInteractor portfolioEntriesInteractor;
    private final GetCurrentValueInteractor currentValueInteractor;
    private final GetInitialInvestmentInteractor initialInvestmentInteractor;
    private final GetTransactionsInteractor transactionsInteractor;
    private final User currentUser;
    private final JFrame frame;

    private final PortfolioController portfolioController;
    private final TransactionsController transactionsController;
    private final CryptoPricesController cryptoPricesController;
    private final NewsSearchController newsSearchController;

    /**
     * Constructor for MainController.
     *
     * @param depositCashInteractor              Handles cash deposits.
     * @param withdrawCashInteractor             Handles cash withdrawals.
     * @param buyCryptoInteractor                Handles buying cryptocurrency.
     * @param calculatePortfolioValueInteractor  Calculates portfolio value.
     * @param fetchCryptoPricesInteractor        Fetches cryptocurrency prices.
     * @param cryptoBuyInteractor                Handles crypto buying operations.
     * @param cryptoSellInteractor               Handles crypto selling operations.
     * @param portfolioEntriesInteractor         Gets portfolio entries.
     * @param currentValueInteractor             Calculates current crypto values.
     * @param initialInvestmentInteractor        Calculates initial investments.
     * @param transactionsInteractor             Gets user transactions.
     * @param newsSearchInteractor               Fetches news data.
     * @param currentUser                        The currently logged-in user.
     * @param frame                              The main application frame.
     */
    public MainController(
            DepositCashInteractor depositCashInteractor,
            WithdrawCashInteractor withdrawCashInteractor,
            BuyCryptoInteractor buyCryptoInteractor,
            CalculatePortfolioValueInteractor calculatePortfolioValueInteractor,
            FetchCryptoPricesInteractor fetchCryptoPricesInteractor,
            BuyCryptoInteractor cryptoBuyInteractor,
            SellCryptoInteractor cryptoSellInteractor,
            GetPortfolioEntriesInteractor portfolioEntriesInteractor,
            GetCurrentValueInteractor currentValueInteractor,
            GetInitialInvestmentInteractor initialInvestmentInteractor,
            GetTransactionsInteractor transactionsInteractor,
            NewsSearchInteractor newsSearchInteractor,
            User currentUser,
            JFrame frame) {
        this.depositCashInteractor = depositCashInteractor;
        this.withdrawCashInteractor = withdrawCashInteractor;
        this.buyCryptoInteractor = buyCryptoInteractor;
        this.calculatePortfolioValueInteractor = calculatePortfolioValueInteractor;
        this.fetchCryptoPricesInteractor = fetchCryptoPricesInteractor;
        this.cryptoBuyInteractor = cryptoBuyInteractor;
        this.cryptoSellInteractor = cryptoSellInteractor;
        this.portfolioEntriesInteractor = portfolioEntriesInteractor;
        this.currentValueInteractor = currentValueInteractor;
        this.initialInvestmentInteractor = initialInvestmentInteractor;
        this.transactionsInteractor = transactionsInteractor;
        this.newsSearchInteractor = newsSearchInteractor;
        this.currentUser = currentUser;
        this.frame = frame;

        // Initialize controllers
        this.portfolioController = initializePortfolioController();
        this.transactionsController = initializeTransactionsController();
        this.cryptoPricesController = initializeCryptoPricesController();
        this.newsSearchController = initializeNewsSearchController();
    }

    /**
     * Starts the main view.
     */
    public void start() {
        mainView = new MainView();

        mainView.setDepositAction(actionEvent -> handleDeposit(mainView));
        mainView.setWithdrawAction(actionEvent -> handleWithdraw(mainView));
        mainView.setBuyCryptoAction(actionEvent -> handleBuyCrypto(mainView));
        mainView.setSellCryptoAction(actionEvent -> handleSellCrypto(mainView));


        setupTabs(mainView);
        updateHeaderValues(mainView);

        frame.setContentPane(mainView);
        frame.revalidate();
    }

    /**
     * Handles selling cryptocurrency.
     *
     * @param mainView The main view instance.
     */
    private void handleSellCrypto(MainView mainView) {
        final String cryptoSymbol = JOptionPane.showInputDialog(frame, "Enter cryptocurrency symbol to sell (e.g., BTC):");
        if (cryptoSymbol == null || cryptoSymbol.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid cryptocurrency symbol.", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String input = JOptionPane.showInputDialog(frame, "Enter amount to sell:");
        try {
            final double amount = Double.parseDouble(input);
            cryptoSellInteractor.execute(currentUser, cryptoSymbol, amount);
            JOptionPane.showMessageDialog(frame,
                    String.format("Successfully sold %.2f units of %s", amount, cryptoSymbol));
            updateHeaderValues(mainView);
            transactionsController.refresh();
            portfolioController.refresh();
        } catch (Exception exception) {
            transactionsController.refresh();
            portfolioController.refresh();
            JOptionPane.showMessageDialog(frame, "Error: " + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Sets up the tabs in the main view.
     *
     * @param mainView The main view instance.
     */
    private void setupTabs(MainView mainView) {
        mainView.addPortfolioPanel(portfolioController.getView());
        mainView.addTransactionsPanel(transactionsController.getView());
        mainView.addCryptoPricesPanel(cryptoPricesController.getView());
        mainView.addNewsPanel(newsSearchController.getView());
    }

    /**
     * Updates the header values in the main view.
     *
     * @param mainView The main view instance.
     */
    public void updateHeaderValues(MainView mainView) {
        mainView.updateCashReserves(currentUser.getCashBalance());
        try {
            double portfolioValue = calculatePortfolioValueInteractor.execute(currentUser);
            mainView.updatePortfolioValue(portfolioValue);
        } catch (Exception exception) {
            mainView.updatePortfolioValue(0.0);
            JOptionPane.showMessageDialog(frame, "Error calculating portfolio value: " + exception.getMessage(),
                    ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles depositing cash.
     *
     * @param mainView The main view instance.
     */
    private void handleDeposit(MainView mainView) {
        final String input = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        try {
            final double amount = Double.parseDouble(input);
            depositCashInteractor.execute(currentUser, amount);
            JOptionPane.showMessageDialog(frame, String.format("Successfully deposited $%.2f", amount));
            updateHeaderValues(mainView);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, ERROR2 + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles withdrawing cash.
     *
     * @param mainView The main view instance.
     */
    private void handleWithdraw(MainView mainView) {
        final String input = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        try {
            final double amount = Double.parseDouble(input);
            withdrawCashInteractor.execute(currentUser, amount);
            JOptionPane.showMessageDialog(frame, String.format("Successfully withdrew $%.2f", amount));
            updateHeaderValues(mainView);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, ERROR2 + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles buying cryptocurrency.
     *
     * @param mainView The main view instance.
     */
    private void handleBuyCrypto(MainView mainView) {
        final String cryptoSymbol = JOptionPane.showInputDialog(frame, "Enter cryptocurrency symbol (e.g., BTC):");
        if (cryptoSymbol == null || cryptoSymbol.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid cryptocurrency symbol.", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String input = JOptionPane.showInputDialog(frame, "Enter amount to buy:");
        try {
            final double amount = Double.parseDouble(input);
            buyCryptoInteractor.execute(currentUser, cryptoSymbol, amount);
            JOptionPane.showMessageDialog(frame,
                    String.format("Successfully bought %.6f units of %s", amount, cryptoSymbol));
            updateHeaderValues(mainView);
            transactionsController.refresh();
            portfolioController.refresh();
        } catch (Exception exception) {
            transactionsController.refresh();
            portfolioController.refresh();
            JOptionPane.showMessageDialog(frame, "Error: " + exception.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initializes the PortfolioController.
     *
     * @return A fully initialized PortfolioController.
     */
    private PortfolioController initializePortfolioController() {
        PortfolioView portfolioView = new PortfolioView();
        return new PortfolioController(
                portfolioEntriesInteractor,
                currentValueInteractor,
                initialInvestmentInteractor,
                currentUser,
                portfolioView
        );
    }

    /**
     * Initializes the TransactionsController.
     *
     * @return A fully initialized TransactionsController.
     */
    private TransactionsController initializeTransactionsController() {
        TransactionsView transactionsView = new TransactionsView();
        return new TransactionsController(
                transactionsInteractor,
                currentUser,
                transactionsView
        );
    }

    /**
     * Initializes the CryptoPricesController.
     *
     * @return A fully initialized CryptoPricesController.
     */
    private CryptoPricesController initializeCryptoPricesController() {
        CryptoPricesView cryptoPricesView = new CryptoPricesView();
        return new CryptoPricesController(
                fetchCryptoPricesInteractor,
                cryptoBuyInteractor,
                cryptoSellInteractor,
                currentUser,
                cryptoPricesView,
                portfolioController,
                transactionsController,
                this,
                mainView
        );
    }

    /**
     * Initializes the NewsSearchController.
     *
     * @return A fully initialized NewsSearchController.
     */
    private NewsSearchController initializeNewsSearchController() {
        NewsSearchView newsSearchView = new NewsSearchView();
        return new NewsSearchController(newsSearchInteractor, newsSearchView);
    }
    public MainView getMainView() {return mainView;}
}
