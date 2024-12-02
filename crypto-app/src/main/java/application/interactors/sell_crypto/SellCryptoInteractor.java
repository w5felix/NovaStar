package application.interactors.sell_crypto;

import domain.entities.PortfolioEntry;
import domain.entities.Transaction;
import domain.entities.User;
import infrastructure.interfaces.CryptoApi;
import infrastructure.interfaces.UserApi;

import java.util.Date;

public class SellCryptoInteractor {

    private final UserApi userApi;
    private final CryptoApi cryptoApi;

    public SellCryptoInteractor(UserApi userApi, CryptoApi cryptoApi) {
        this.userApi = userApi;
        this.cryptoApi = cryptoApi;
    }

    public void execute(User user, String cryptoName, double amount) throws Exception {
        PortfolioEntry entry = user.getPortfolio().stream()
                .filter(e -> e.getCryptoName().equals(cryptoName))
                .findFirst()
                .orElse(null);

        if (entry == null || entry.getAmount() < amount) {
            throw new IllegalArgumentException("Insufficient holdings to sell " + cryptoName);
        }

        double price = cryptoApi.getCurrentPrice(cryptoName + "-USD");
        double revenue = amount * price;

        // Create and add the transaction locally
        Transaction transaction = new Transaction(cryptoName, amount, price, new Date(), "SELL");
        user.getTransactions().add(transaction); // Update the user object directly

        // Persist the transaction and portfolio update to the backend
        userApi.addTransactionAndUpdatePortfolio(user.getUserId(), transaction);

        // Update the portfolio entry
        entry.subtractAmount(amount);

        // Remove the entry from the portfolio if the amount is zero
        if (entry.getAmount() == 0) {
            user.getPortfolio().remove(entry);
        }

        // Update the cash balance
        user.setCashBalance(user.getCashBalance() + revenue);
    }
}
