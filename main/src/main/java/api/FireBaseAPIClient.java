package api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.PortfolioEntry;
import entities.Transaction;
import okhttp3.*;
import java.math.BigDecimal;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class FireBaseAPIClient {

    private static final String API_KEY = "AIzaSyBZ_meIO9GRHaLjHfq3B5WZs0v--HB1wrQ";
    private static final String DATABASE_URL = "https://dogedemo-83ba8-default-rtdb.firebaseio.com/";
    private static final OkHttpClient client = new OkHttpClient();

    // User registration
    public static String registerUser(String email, String password) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("email", email);
        requestBody.addProperty("password", password);
        requestBody.addProperty("returnSecureToken", true);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JsonObject jsonResponse = JsonParser.parseString(response.body().string()).getAsJsonObject();
                return jsonResponse.get("localId").getAsString(); // User ID
            } else {
                throw new IOException("Error registering user: " + response.body().string());
            }
        }
    }

    // User login
    public static String loginUser(String email, String password) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("email", email);
        requestBody.addProperty("password", password);
        requestBody.addProperty("returnSecureToken", true);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JsonObject jsonResponse = JsonParser.parseString(response.body().string()).getAsJsonObject();
                return jsonResponse.get("localId").getAsString(); // User ID
            } else {
                throw new IOException("Error logging in: " + response.body().string());
            }
        }
    }

    // Add cash
    public static void addCash(String userId, double amount) throws IOException {
        double currentBalance = getCashReserves(userId);
        updateCashReserves(userId, currentBalance + amount);
    }

    // Withdraw cash
    public static void withdrawCash(String userId, double amount) throws IOException {
        double currentBalance = getCashReserves(userId);
        if (currentBalance < amount) {
            throw new IllegalArgumentException("Insufficient funds to withdraw.");
        }
        updateCashReserves(userId, currentBalance - amount);
    }

    // Get cash reserves
    public static double getCashReserves(String userId) throws IOException {
        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + "/cashReserves.json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                if (responseBody == null || responseBody.equals("null")) {
                    return 0.0;
                }

                // Parse the JSON response
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                return jsonResponse.get("cashReserves").getAsDouble();
            } else {
                throw new IOException("Error fetching cash reserves: " + response.body().string());
            }
        }
    }

    // Update cash reserves
    private static void updateCashReserves(String userId, double newBalance) throws IOException {
        JsonObject cashData = new JsonObject();
        cashData.addProperty("cashReserves", newBalance);

        RequestBody body = RequestBody.create(
                cashData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + "/cashReserves.json")
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error updating cash reserves: " + response.body().string());
            }
        }
    }



    public static void addTransactionAndUpdatePortfolio(String userId, Transaction transaction) throws IOException {
        // Fetch current cash reserves
        BigDecimal cashReserves = BigDecimal.valueOf(getCashReserves(userId));
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId);

        if (transaction.getType().equalsIgnoreCase("BUY")) {
            BigDecimal totalCost = BigDecimal.valueOf(transaction.getAmount())
                    .multiply(BigDecimal.valueOf(transaction.getPrice())); // Total cost = amount * price
            if (cashReserves.compareTo(totalCost) < 0) {
                throw new IllegalArgumentException("Insufficient cash reserves to complete the transaction.");
            }
            cashReserves = cashReserves.subtract(totalCost); // Deduct total cost
        } else if (transaction.getType().equalsIgnoreCase("SELL")) {
            PortfolioEntry entry = portfolio.getOrDefault(transaction.getCryptoName(),
                    new PortfolioEntry(transaction.getCryptoName(), 0));
            if (entry.getAmount() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient holdings to sell " + transaction.getCryptoName());
            }
            BigDecimal totalRevenue = BigDecimal.valueOf(transaction.getAmount())
                    .multiply(BigDecimal.valueOf(transaction.getPrice())); // Total revenue = amount * price
            cashReserves = cashReserves.add(totalRevenue); // Add total revenue
        }

        // Update portfolio and Firebase
        updatePortfolio(userId, transaction);
        updateCashReserves(userId, cashReserves.doubleValue()); // Convert BigDecimal back to double

        // Record the transaction in Firebase
        addTransaction(userId, transaction);
    }


    // Add a transaction
    private static void addTransaction(String userId, Transaction transaction) throws IOException {
        JsonObject transactionData = new JsonObject();
        transactionData.addProperty("cryptoName", transaction.getCryptoName());
        transactionData.addProperty("amount", transaction.getAmount());
        transactionData.addProperty("price", transaction.getPrice());
        transactionData.addProperty("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(transaction.getDate()));
        transactionData.addProperty("type", transaction.getType());

        RequestBody body = RequestBody.create(
                transactionData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + "/transactions.json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error adding transaction: " + response.body().string());
            }
        }
    }

    public static List<Transaction> getTransactions(String userId) throws IOException {
        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + "/transactions.json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                if (responseBody == null || responseBody.equals("null")) {
                    return new ArrayList<>();
                }

                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                List<Transaction> transactions = new ArrayList<>();

                jsonResponse.entrySet().forEach(entry -> {
                    JsonObject transactionData = entry.getValue().getAsJsonObject();
                    try {
                        String cryptoName = transactionData.get("cryptoName").getAsString();
                        double amount = transactionData.get("amount").getAsDouble();
                        double price = transactionData.get("price").getAsDouble();
                        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(transactionData.get("date").getAsString());
                        String type = transactionData.get("type").getAsString();

                        transactions.add(new Transaction(cryptoName, amount, price, date, type));
                    } catch (Exception e) {
                        System.err.println("Error parsing transaction: " + e.getMessage());
                    }
                });

                return transactions;
            } else {
                throw new IOException("Error fetching transactions: " + response.body().string());
            }
        }
    }

    // Get full portfolio as a Map
    public static Map<String, PortfolioEntry> getPortfolio(String userId) throws IOException {
        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + "/portfolio.json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Map<String, PortfolioEntry> portfolio = new HashMap<>();
                if (responseBody == null || responseBody.equals("null")) {
                    return portfolio;
                }

                // Parse the JSON response
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                jsonResponse.entrySet().forEach(entry -> {
                    JsonObject entryData = entry.getValue().getAsJsonObject();
                    String cryptoName = entry.getKey();
                    double amount = entryData.get("amount").getAsDouble();
                    portfolio.put(cryptoName, new PortfolioEntry(cryptoName, amount));
                });

                return portfolio;
            } else {
                throw new IOException("Error fetching portfolio: " + response.body().string());
            }
        }
    }

    // Update portfolio
    private static void updatePortfolio(String userId, Transaction transaction) throws IOException {
        // Fetch the current portfolio from Firebase
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId);

        // Find or create a portfolio entry for the cryptocurrency
        PortfolioEntry entry = portfolio.getOrDefault(transaction.getCryptoName(), new PortfolioEntry(transaction.getCryptoName(), 0));

        // Update the portfolio based on transaction type
        if (transaction.getType().equalsIgnoreCase("BUY")) {
            entry.addAmount(transaction.getAmount()); // Add the purchased amount
            portfolio.put(transaction.getCryptoName(), entry); // Save it back to the portfolio map
        } else if (transaction.getType().equalsIgnoreCase("SELL")) {
            entry.subtractAmount(transaction.getAmount()); // Subtract the sold amount

            // Remove the entry if the amount reaches zero
            if (entry.getAmount() <= 0) {
                portfolio.remove(transaction.getCryptoName());
            } else {
                portfolio.put(transaction.getCryptoName(), entry); // Update the portfolio map
            }
        }

        // Prepare the JSON payload for Firebase
        JsonObject portfolioData = new JsonObject();
        for (Map.Entry<String, PortfolioEntry> portfolioEntry : portfolio.entrySet()) {
            JsonObject entryData = new JsonObject();
            entryData.addProperty("amount", portfolioEntry.getValue().getAmount());
            portfolioData.add(portfolioEntry.getKey(), entryData); // Add each cryptocurrency entry
        }

        // Send the updated portfolio data to Firebase
        RequestBody body = RequestBody.create(
                portfolioData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + "/portfolio.json")
                .put(body)
                .build();

        // Execute the request and check for errors
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error updating portfolio in Firebase: " + response.body().string());
            }
        }
    }


    public static List<PortfolioEntry> getPortfolioEntries(String userId) throws IOException {
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId); // Use existing `getPortfolio` method
        return new ArrayList<>(portfolio.values()); // Convert the portfolio map to a list
    }

    public static PortfolioEntry getPortfolioEntry(String userId, String cryptoName) throws IOException {
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId); // Use existing `getPortfolio` method
        return portfolio.getOrDefault(cryptoName, new PortfolioEntry(cryptoName, 0)); // Return the entry or a default one
    }



}
