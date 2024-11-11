package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BlockChainAPIClient {

    private static final String API_KEY = "8dea783c-d24c-4f6b-ad1d-87bc129e9feb";
    private static final String BASE_URL = "https://api.blockchain.com/v3/exchange";

    public static void main(String[] args) {
        try {
            String response = fetchTickers();
            parseAndDisplay(response);
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

    private static void parseAndDisplay(String jsonResponse) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(jsonResponse);

            for (Object obj : jsonArray) {
                JSONObject ticker = (JSONObject) obj;
                String symbol = (String) ticker.get("symbol");
                Double price24h = (Double) ticker.get("price_24h");
                Double lastTradePrice = (Double) ticker.get("last_trade_price");

                System.out.printf("Symbol: %s | 24h Price: %.2f | Last Trade Price: %.2f%n", symbol, price24h, lastTradePrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
