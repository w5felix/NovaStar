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
import application.controllers.LoginController;
import infrastructure.adapters.FirebaseServiceAdapter;
import infrastructure.adapters.BlockchainServiceAdapter;
import application.interactors.news_search.NewsSearchInteractor;
import application.interactors.news_search.NewsSearchDataAccessInterface;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize Adapters (Dependency Layer)
            FirebaseServiceAdapter userApi = new FirebaseServiceAdapter();
            BlockchainServiceAdapter cryptoApi = new BlockchainServiceAdapter();

            // Initialize News Service
            NewsSearchDataAccessInterface newsService = new NewsSearchInteractor();

            // Initialize Interactors (Use Cases Layer)
            LoginUserInteractor loginInteractor = new LoginUserInteractor(userApi);
            RegisterUserInteractor registerInteractor = new RegisterUserInteractor(userApi);
            ResetPasswordInteractor resetPasswordInteractor = new ResetPasswordInteractor(userApi);
            DepositCashInteractor depositCashInteractor = new DepositCashInteractor(userApi);
            WithdrawCashInteractor withdrawCashInteractor = new WithdrawCashInteractor(userApi);
            BuyCryptoInteractor buyCryptoInteractor = new BuyCryptoInteractor(userApi, cryptoApi);
            CalculatePortfolioValueInteractor portfolioValueInteractor = new CalculatePortfolioValueInteractor(cryptoApi);
            GetPortfolioEntriesInteractor portfolioEntriesInteractor = new GetPortfolioEntriesInteractor(userApi);
            GetCurrentValueInteractor currentValueInteractor = new GetCurrentValueInteractor(cryptoApi);
            GetInitialInvestmentInteractor initialInvestmentInteractor = new GetInitialInvestmentInteractor();
            GetTransactionsInteractor transactionsInteractor = new GetTransactionsInteractor(userApi);
            FetchCryptoPricesInteractor fetchCryptoPricesInteractor = new FetchCryptoPricesInteractor();
            BuyCryptoInteractor cryptoBuyInteractor = new BuyCryptoInteractor(userApi, cryptoApi);
            SellCryptoInteractor cryptoSellInteractor = new SellCryptoInteractor(userApi, cryptoApi);
            NewsSearchInteractor newsSearchInteractor = new NewsSearchInteractor();

            // Set up the main application frame (UI Layer)
            JFrame frame = new JFrame("Crypto Portfolio Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);

            // Initialize and start the LoginController
            LoginController loginController = new LoginController(
                    loginInteractor,
                    registerInteractor,
                    resetPasswordInteractor,
                    depositCashInteractor,
                    withdrawCashInteractor,
                    buyCryptoInteractor,
                    portfolioValueInteractor,
                    fetchCryptoPricesInteractor,
                    cryptoBuyInteractor,
                    cryptoSellInteractor,
                    portfolioEntriesInteractor,
                    currentValueInteractor,
                    initialInvestmentInteractor,
                    transactionsInteractor,
                    newsSearchInteractor,
                    userApi,  // Pass the User API adapter
                    newsService,  // Pass the News Service
                    frame
            );

            // Start the login process
            loginController.start();

            // Make the frame visible
            frame.setVisible(true);
        });
    }
}
