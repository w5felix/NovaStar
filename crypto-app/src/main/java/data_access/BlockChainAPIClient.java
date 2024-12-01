package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A client for interacting with the Blockchain.com Exchange API.
 *
 * This client allows fetching cryptocurrency ticker data and provides utility
 * methods for retrieving the current price and a list of popular cryptocurrencies.
 */
public class BlockChainAPIClient {

    private static final String API_KEY = "8dea783c-d24c-4f6b-ad1d-87bc129e9feb"; // Replace with your API key
    private static final String BASE_URL = "https://api.blockchain.com/v3/exchange";

    /**
     * Main method demonstrating the usage of the client.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // Fetch and display all popular cryptocurrencies.
            List<CryptoInfo> cryptos = fetchPopularCryptos();
            for (CryptoInfo crypto : cryptos) {
                System.out.printf("%s (%s): $%.2f (24h: %.2f%%)%n",
                        crypto.getName(), crypto.getSymbol(), crypto.getCurrentPrice(), crypto.getPercentageChange());
            }
        } catch (Exception e) {
            System.err.println("An error occurred while fetching cryptocurrency data.");
            e.printStackTrace();
        }
    }

    /**
     * Makes an HTTP GET request to the tickers endpoint of the Blockchain.com API.
     *
     * @return JSON response as a String.
     * @throws Exception if an error occurs during the HTTP request.
     */
    private static String fetchTickers() throws Exception {
        String endpoint = BASE_URL + "/tickers";
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("X-API-Token", API_KEY);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        }
    }

    /**
     * Retrieves the current price of a specific cryptocurrency.
     *
     * @param cryptoSymbol The symbol of the cryptocurrency (e.g., "BTC-USD").
     * @return The current price as a float.
     * @throws Exception if the symbol is not found or an error occurs during the API call.
     */
    public static float getCurrentPrice(String cryptoSymbol) throws Exception {
        String response = fetchTickers();
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(response, JsonArray.class);

        for (JsonElement element : jsonArray) {
            JsonObject ticker = element.getAsJsonObject();
            String symbol = ticker.get("symbol").getAsString();

            if (symbol.equals(cryptoSymbol)) {
                return ticker.get("last_trade_price").getAsFloat();
            }
        }

        throw new Exception("Symbol not found: " + cryptoSymbol);
    }

    /**
     * Fetches information about popular cryptocurrencies.
     * Filters USD trading pairs and calculates 24-hour percentage change.
     *
     * @return a List of CryptoInfo objects containing cryptocurrency details.
     * @throws Exception if an error occurs during the API call or data parsing.
     */
    public static List<CryptoInfo> fetchPopularCryptos() throws Exception {
        String response = fetchTickers();
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(response, JsonArray.class);

        List<CryptoInfo> cryptoList = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject ticker = element.getAsJsonObject();
            String symbol = ticker.get("symbol").getAsString();
            float lastTradePrice = ticker.get("last_trade_price").getAsFloat();
            float price24h = ticker.has("price_24h") ? ticker.get("price_24h").getAsFloat() : lastTradePrice;
            float percentageChange = ((lastTradePrice - price24h) / price24h) * 100;

            // Add popular crypto symbols only (e.g., BTC, ETH, LTC).
            if (symbol.endsWith("-USD")) { // Filter for USD trading pairs
                cryptoList.add(new CryptoInfo(
                        symbol.replace("-USD", ""), // Extract name (e.g., BTC)
                        symbol, // Full symbol (e.g., BTC-USD)
                        lastTradePrice, // Current price
                        percentageChange // 24-hour percentage change
                ));
            }
        }

        return cryptoList;
    }

    /**
     * A simple data class representing cryptocurrency information.
     */
    public static class CryptoInfo {
        private final String name;
        private final String symbol;
        private final float currentPrice;
        private final float percentageChange;

        /**
         * Constructor for creating a CryptoInfo object.
         *
         * @param name             The name of the cryptocurrency (e.g., "BTC").
         * @param symbol           The symbol of the cryptocurrency (e.g., "BTC-USD").
         * @param currentPrice     The current trading price.
         * @param percentageChange The percentage change in price over the last 24 hours.
         */
        public CryptoInfo(String name, String symbol, float currentPrice, float percentageChange) {
            this.name = name;
            this.symbol = symbol;
            this.currentPrice = currentPrice;
            this.percentageChange = percentageChange;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }

        public float getCurrentPrice() {
            return currentPrice;
        }

        public float getPercentageChange() {
            return percentageChange;
        }
    }
}
