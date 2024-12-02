package interface_adapterss;

import api.BlockChainAPIClient;

/**
 * Adapter for Blockchain API, implementing CryptoApi interface.
 */
public class BlockchainServiceAdapter implements CryptoApi {

    @Override
    public float getCurrentPrice(String cryptoSymbol) throws Exception {
        // Delegates the call to BlockChainAPIClient
        return BlockChainAPIClient.getCurrentPrice(cryptoSymbol);
    }
}
