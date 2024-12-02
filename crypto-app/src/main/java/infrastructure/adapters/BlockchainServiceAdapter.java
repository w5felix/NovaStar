package infrastructure.adapters;

import infrastructure.api_clients.BlockChainApiClient;
import infrastructure.interfaces.CryptoApi;

/**
 * Adapter for Blockchain API, implementing CryptoApi interface.
 */
public class BlockchainServiceAdapter implements CryptoApi {

    @Override
    public float getCurrentPrice(String cryptoSymbol) throws Exception {
        // Delegates the call to BlockChainAPIClient
        return BlockChainApiClient.getCurrentPrice(cryptoSymbol);
    }
}
