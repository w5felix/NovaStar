package application.interactors.calculate_portfolio_value;

import domain.entities.PortfolioEntry;
import domain.entities.User;
import infrastructure.interfaces.CryptoApi;

public class CalculatePortfolioValueInteractor {
    private final CryptoApi cryptoApi;

    public CalculatePortfolioValueInteractor(CryptoApi cryptoApi) {
        this.cryptoApi = cryptoApi;
    }

    public double execute(User user) throws Exception {
        double totalValue = user.getCashBalance();
        for (PortfolioEntry entry : user.getPortfolio()) {
            totalValue += entry.getAmount() * cryptoApi.getCurrentPrice(entry.getCryptoName() + "-USD");
        }
        return totalValue;
    }
}
