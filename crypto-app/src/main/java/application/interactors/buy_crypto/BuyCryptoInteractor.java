package application.interactors.buy_crypto;

import domain.entities.PortfolioEntry;
import domain.entities.Transaction;
import domain.entities.User;
import infrastructure.interfaces.CryptoApi;
import infrastructure.interfaces.UserApi;

import java.util.Date;

public class BuyCryptoInteractor {

    private final UserApi userApi;
    private final CryptoApi cryptoApi;

    public BuyCryptoInteractor(UserApi userApi, CryptoApi cryptoApi) {
        this.userApi = userApi;
        this.cryptoApi = cryptoApi;
    }

    public void execute(User user, String cryptoName, double amount) throws Exception {
        double price = cryptoApi.getCurrentPrice(cryptoName + "-USD");
        double cost = amount * price;

        if (user.getCashBalance() < cost) {
            throw new IllegalArgumentException("Insufficient cash balance to buy " + cryptoName);
        }

        // Deduct the cost from the user's cash balance
        user.setCashBalance(user.getCashBalance() - cost);

        // Create and add the transaction locally
        Transaction transaction = new Transaction(cryptoName, amount, price, new Date(), "BUY");
        user.getTransactions().add(transaction); // Update the user object directly

        // Persist the transaction and portfolio update to the backend
        userApi.addTransactionAndUpdatePortfolio(user.getUserId(), transaction);

        // Update the portfolio entry
        PortfolioEntry entry = user.getPortfolio().stream()
                .filter(e -> e.getCryptoName().equals(cryptoName))
                .findFirst()
                .orElse(null);

        if (entry != null) {
            // Add the purchased amount to the existing entry
            entry.addAmount(amount);
        } else {
            // Create a new portfolio entry if it's a new cryptocurrency
            user.getPortfolio().add(new PortfolioEntry(cryptoName, amount));
        }
    }
}
