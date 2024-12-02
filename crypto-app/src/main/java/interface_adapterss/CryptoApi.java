package interface_adapterss;

/**
 * Interface for cryptocurrency API operations.
 */
public interface CryptoApi {
    /**
     * Retrieves the current price of a cryptocurrency.
     *
     * @param cryptoSymbol The symbol of the cryptocurrency (e.g., "BTC-USD").
     * @return The current price as a float.
     * @throws Exception If an error occurs during the API call.
     */
    float getCurrentPrice(String cryptoSymbol) throws Exception;
}
