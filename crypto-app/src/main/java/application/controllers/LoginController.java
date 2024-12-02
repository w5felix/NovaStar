package application.controllers;

import application.interactors.buy_crypto.BuyCryptoInteractor;
import application.interactors.calculate_portfolio_value.CalculatePortfolioValueInteractor;
import application.interactors.deposit_cash.DepositCashInteractor;
import application.interactors.fetch_crypto_prices.FetchCryptoPricesInteractor;
import application.interactors.get_current_value.GetCurrentValueInteractor;
import application.interactors.get_initial_investment.GetInitialInvestmentInteractor;
import application.interactors.get_portfolio_entries.GetPortfolioEntriesInteractor;
import application.interactors.get_transactions.GetTransactionsInteractor;
import application.interactors.login_user.LoginUserInteractor;
import application.interactors.register_user.RegisterUserInteractor;
import application.interactors.reset_password.ResetPasswordInteractor;
import application.interactors.sell_crypto.SellCryptoInteractor;
import application.interactors.withdraw_cash.WithdrawCashInteractor;
import domain.entities.User;
import infrastructure.interfaces.UserApi;
import application.interactors.news_search.NewsSearchDataAccessInterface;
import application.interactors.news_search.NewsSearchInteractor;
import views.LoginView;

import javax.swing.*;

public class LoginController {

    public static final String ERROR = "Error: ";

    private final LoginUserInteractor loginInteractor;
    private final RegisterUserInteractor registerInteractor;
    private final ResetPasswordInteractor resetPasswordInteractor;

    // Dependencies for MainController initialization
    private final DepositCashInteractor depositCashInteractor;
    private final WithdrawCashInteractor withdrawCashInteractor;
    private final BuyCryptoInteractor buyCryptoInteractor;
    private final CalculatePortfolioValueInteractor calculatePortfolioValueInteractor;
    private final FetchCryptoPricesInteractor fetchCryptoPricesInteractor;
    private final BuyCryptoInteractor cryptoBuyInteractor;
    private final SellCryptoInteractor cryptoSellInteractor;
    private final GetPortfolioEntriesInteractor portfolioEntriesInteractor;
    private final GetCurrentValueInteractor currentValueInteractor;
    private final GetInitialInvestmentInteractor initialInvestmentInteractor;
    private final GetTransactionsInteractor transactionsInteractor;
    private final NewsSearchInteractor newsSearchInteractor;

    private final UserApi userApi;
    private final NewsSearchDataAccessInterface newsService;
    private final JFrame frame;

    public LoginController(LoginUserInteractor loginInteractor,
                           RegisterUserInteractor registerInteractor,
                           ResetPasswordInteractor resetPasswordInteractor,
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
                           UserApi userApi,
                           NewsSearchDataAccessInterface newsService,
                           JFrame frame) {
        this.loginInteractor = loginInteractor;
        this.registerInteractor = registerInteractor;
        this.resetPasswordInteractor = resetPasswordInteractor;
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
        this.userApi = userApi;
        this.newsService = newsService;
        this.frame = frame;
    }

    public void start() {
        final LoginView loginView = new LoginView();

        loginView.addLoginListener(actionEvent -> handleLogin(loginView));
        loginView.addRegisterListener(actionEvent -> handleRegister(loginView));
        loginView.addResetPasswordListener(actionEvent -> handleResetPassword(loginView));

        frame.setContentPane(loginView);
        frame.revalidate();
    }

    private void handleLogin(LoginView loginView) {
        final String email = loginView.getEmail();
        final String password = loginView.getPassword();
        try {
            final User currentUser = loginInteractor.execute(email, password);
            showMainView(currentUser);
        } catch (Exception exception) {
            String firebaseResponse = exception.getMessage(); // Assume message is JSON
            loginView.displayErrorMessage(firebaseResponse);
        }
    }

    private void handleRegister(LoginView loginView) {
        final String email = loginView.getEmail();
        final String password = loginView.getPassword();
        try {
            final User currentUser = registerInteractor.execute(
                    "New User", email, password,
                    "What is your favorite color?", "Blue");
            showMainView(currentUser);
        } catch (Exception exception) {
            String firebaseResponse = exception.getMessage(); // Assume message is JSON
            loginView.displayErrorMessage(firebaseResponse);
        }
    }

    private void handleResetPassword(LoginView loginView) {
        final String email = loginView.getEmail();
        try {
            resetPasswordInteractor.execute(email);
            loginView.setStatusMessage("If this email is associated with an account, it will receive a link to reset the password.");
        } catch (Exception exception) {
            String firebaseResponse = exception.getMessage(); // Assume message is JSON
            loginView.displayErrorMessage(firebaseResponse);
        }
    }

    private void showMainView(User currentUser) {
        MainController mainController = createMainController(currentUser);
        mainController.start();
    }

    private MainController createMainController(User currentUser) {
        return new MainController(
                depositCashInteractor,
                withdrawCashInteractor,
                buyCryptoInteractor,
                calculatePortfolioValueInteractor,
                fetchCryptoPricesInteractor,
                cryptoBuyInteractor,
                cryptoSellInteractor,
                portfolioEntriesInteractor,
                currentValueInteractor,
                initialInvestmentInteractor,
                transactionsInteractor,
                newsSearchInteractor,
                currentUser,
                frame
        );
    }
}
