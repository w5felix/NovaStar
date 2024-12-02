package application.interactors.fetch_crypto_prices;

import infrastructure.api_clients.BlockChainApiClient;
import infrastructure.api_clients.BlockChainApiClient.CryptoInfo;

import java.util.List;

public class FetchCryptoPricesInteractor {

    public List<CryptoInfo> execute() throws Exception {
        return BlockChainApiClient.fetchPopularCryptos();
    }
}
