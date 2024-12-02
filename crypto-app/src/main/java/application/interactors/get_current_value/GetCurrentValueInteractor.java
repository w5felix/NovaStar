package application.interactors.get_current_value;

import domain.entities.PortfolioEntry;
import infrastructure.interfaces.CryptoApi;

public class GetCurrentValueInteractor {
    private final CryptoApi cryptoApi;

    public GetCurrentValueInteractor(CryptoApi cryptoApi) {
        this.cryptoApi = cryptoApi;
    }

    public double execute(PortfolioEntry entry) throws Exception {
        return cryptoApi.getCurrentPrice(entry.getCryptoName() + "-USD") * entry.getAmount();
    }
}
