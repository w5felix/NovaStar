package api;

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

public class BlockChainAPIClient {

    private static final String API_KEY = "8dea783c-d24c-4f6b-ad1d-87bc129e9feb";
    private static final String BASE_URL = "https://api.blockchain.com/v3/exchange";

    public static void main(String[] args) {
        try {
            // Example: Fetch all popular cryptocurrencies
            List<CryptoInfo> cryptos = fetchPopularCryptos();
            for (CryptoInfo crypto : cryptos) {
                System.out.printf("%s (%s): $%.2f (24h: %.2f%%)%n",
                        crypto.getName(), crypto.getSymbol(), crypto.getCurrentPrice(), crypto.getPercentageChange());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchTickers() throws Exception {
        String endpoint = BASE_URL + "/tickers";
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("X-API-Token", API_KEY);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

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

            // Add popular crypto symbols only (e.g., BTC, ETH, LTC)
            if (symbol.endsWith("-USD")) { // Filter USD trading pairs
                cryptoList.add(new CryptoInfo(
                        symbol.replace("-USD", ""), // Name (e.g., BTC)
                        symbol, // Symbol (e.g., BTC-USD)
                        lastTradePrice, // Current price
                        percentageChange // 24-hour percentage change
                ));
            }
        }

        return cryptoList;
    }

    // Class to hold cryptocurrency information
    public static class CryptoInfo {
        private final String name;
        private final String symbol;
        private final float currentPrice;
        private final float percentageChange;

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
