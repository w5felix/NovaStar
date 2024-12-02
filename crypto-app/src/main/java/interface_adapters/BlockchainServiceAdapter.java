package interface_adapters;

import data_access.BlockChainApiClient;
import interfaces.CryptoApi;

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
