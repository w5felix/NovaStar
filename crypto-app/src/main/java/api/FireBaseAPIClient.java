package api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.PortfolioEntry;
import entities.Transaction;
import entities.User;
import okhttp3.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;

/**
 * A client for interacting with Firebase Realtime Database and Firebase Authentication APIs.
 * Provides methods for user registration, login, and managing user financial portfolios.
 */
public class FireBaseAPIClient {

    private static final String API_KEY = "AIzaSyBZ_meIO9GRHaLjHfq3B5WZs0v--HB1wrQ"; // Firebase API key
    private static final String DATABASE_URL = "https://dogedemo-83ba8-default-rtdb.firebaseio.com/"; // Firebase DB URL
    private static final OkHttpClient client = new OkHttpClient(); // HTTP client for making requests

    // User registration

    /**
     * Registers a new user with email and password.
     *
     * @param email    User's email address.
     * @param password User's chosen password.
     * @return The user's unique ID (localId) from Firebase.
     * @throws IOException if the registration fails or the server returns an error.
     */
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
                return jsonResponse.get("localId").getAsString();
            } else {
                throw new IOException("Error registering user: " + response.body().string());
            }
        }
    }

    /**
     * Registers a new user with additional details: username, security question, and answer.
     *
     * @param username             User's username.
     * @param email                User's email address.
     * @param password             User's chosen password.
     * @param securityQuestion     User's security question for account recovery.
     * @param securityQuestionAnswer User's answer to the security question.
     * @return The user's unique ID (localId) from Firebase.
     * @throws IOException if the registration fails or the server returns an error.
     */
    public static String registerUser(String username, String email, String password, String securityQuestion,
                                      String securityQuestionAnswer) throws IOException {
        // Step 1: Register the user with Firebase Authentication
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
                String userId = jsonResponse.get("localId").getAsString();

                // Step 2: Store additional details in Firebase Realtime Database
                JsonObject userDetails = new JsonObject();
                userDetails.addProperty("username", username);
                userDetails.addProperty("email", email);
                userDetails.addProperty("securityQuestion", securityQuestion);
                userDetails.addProperty("securityQuestionAnswer", securityQuestionAnswer);

                RequestBody userDetailsBody = RequestBody.create(
                        userDetails.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request updateRequest = new Request.Builder()
                        .url(DATABASE_URL + "users/" + userId + ".json")
                        .put(userDetailsBody)
                        .build();

                try (Response updateResponse = client.newCall(updateRequest).execute()) {
                    if (!updateResponse.isSuccessful()) {
                        throw new IOException("Error updating user details: " + updateResponse.body().string());
                    }
                }

                return userId;
            } else {
                throw new IOException("Error registering user: " + response.body().string());
            }
        }
    }




    // User login

    /**
     * Logs in a user using email and password.
     *
     * @param email    User's email address.
     * @param password User's password.
     * @return The user's unique ID (localId) from Firebase.
     * @throws IOException if the login fails or the server returns an error.
     */
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
                return jsonResponse.get("localId").getAsString();
            } else {
                throw new IOException("Error logging in: " + response.body().string());
            }
        }
    }

    // Add cash

    /**
     * Adds cash to a user's account.
     *
     * @param userId User's unique ID.
     * @param amount The amount to add.
     * @throws IOException if the operation fails.
     */
    public static void addCash(String userId, double amount) throws IOException {
        double currentBalance = getCashReserves(userId);
        updateCashReserves(userId, currentBalance + amount);
    }

    // Withdraw cash

    /**
     * Withdraws cash from a user's account.
     *
     * @param userId User's unique ID.
     * @param amount The amount to withdraw.
     * @throws IOException              if the operation fails.
     * @throws IllegalArgumentException if insufficient funds are available.
     */
    public static void withdrawCash(String userId, double amount) throws IOException {
        double currentBalance = getCashReserves(userId);
        if (currentBalance < amount) {
            throw new IllegalArgumentException("Insufficient funds to withdraw.");
        }
        updateCashReserves(userId, currentBalance - amount);
    }

    // Get cash reserves

    /**
     * Retrieves a user's cash reserves.
     *
     * @param userId User's unique ID.
     * @return The current cash reserves.
     * @throws IOException if the operation fails.
     */
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

                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                return jsonResponse.get("cashReserves").getAsDouble();
            } else {
                throw new IOException("Error fetching cash reserves: " + response.body().string());
            }
        }
    }

    // Update cash reserves

    /**
     * Updates a user's cash reserves in the database.
     *
     * @param userId      User's unique ID.
     * @param newBalance  The new cash reserve balance.
     * @throws IOException if the operation fails.
     */
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

    // Transaction and portfolio management

    /**
     * Handles a transaction and updates the portfolio and cash reserves accordingly.
     *
     * @param userId      User's unique ID.
     * @param transaction The transaction to be executed.
     * @throws IOException if the operation fails.
     */
    public static void addTransactionAndUpdatePortfolio(String userId, Transaction transaction) throws IOException {
        BigDecimal cashReserves = BigDecimal.valueOf(getCashReserves(userId));
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId);

        if (transaction.getType().equalsIgnoreCase("BUY")) {
            BigDecimal totalCost = BigDecimal.valueOf(transaction.getAmount())
                    .multiply(BigDecimal.valueOf(transaction.getPrice()));
            if (cashReserves.compareTo(totalCost) < 0) {
                throw new IllegalArgumentException("Insufficient cash reserves to complete the transaction.");
            }
            cashReserves = cashReserves.subtract(totalCost);
        } else if (transaction.getType().equalsIgnoreCase("SELL")) {
            PortfolioEntry entry = portfolio.getOrDefault(transaction.getCryptoName(),
                    new PortfolioEntry(transaction.getCryptoName(), 0));
            if (entry.getAmount() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient holdings to sell " + transaction.getCryptoName());
            }
            BigDecimal totalRevenue = BigDecimal.valueOf(transaction.getAmount())
                    .multiply(BigDecimal.valueOf(transaction.getPrice()));
            cashReserves = cashReserves.add(totalRevenue);
        }

        updatePortfolio(userId, transaction);
        updateCashReserves(userId, cashReserves.doubleValue());
        addTransaction(userId, transaction);
    }

    /**
     * Fetches all transactions for a user.
     *
     * @param userId User's unique ID.
     * @return A list of Transaction objects.
     * @throws IOException if the operation fails.
     */
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

    /**
     * Fetches the user's portfolio as a map.
     *
     * @param userId User's unique ID.
     * @return A map of cryptocurrency names to PortfolioEntry objects.
     * @throws IOException if the operation fails.
     */
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

    /**
     * Updates the user's portfolio in Firebase after a transaction.
     *
     * @param userId      User's unique ID.
     * @param transaction The transaction to apply to the portfolio.
     * @throws IOException if the operation fails.
     */
    private static void updatePortfolio(String userId, Transaction transaction) throws IOException {
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId);

        PortfolioEntry entry = portfolio.getOrDefault(transaction.getCryptoName(), new PortfolioEntry(transaction.getCryptoName(), 0));

        if (transaction.getType().equalsIgnoreCase("BUY")) {
            entry.addAmount(transaction.getAmount());
            portfolio.put(transaction.getCryptoName(), entry);
        } else if (transaction.getType().equalsIgnoreCase("SELL")) {
            entry.subtractAmount(transaction.getAmount());
            if (entry.getAmount() <= 0) {
                portfolio.remove(transaction.getCryptoName());
            } else {
                portfolio.put(transaction.getCryptoName(), entry);
            }
        }

        JsonObject portfolioData = new JsonObject();
        for (Map.Entry<String, PortfolioEntry> portfolioEntry : portfolio.entrySet()) {
            JsonObject entryData = new JsonObject();
            entryData.addProperty("amount", portfolioEntry.getValue().getAmount());
            portfolioData.add(portfolioEntry.getKey(), entryData);
        }

        RequestBody body = RequestBody.create(
                portfolioData.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + "/portfolio.json")
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error updating portfolio in Firebase: " + response.body().string());
            }
        }
    }

    /**
     * Fetches a user's portfolio entries as a list.
     *
     * @param userId User's unique ID.
     * @return A list of PortfolioEntry objects.
     * @throws IOException if the operation fails.
     */
    public static List<PortfolioEntry> getPortfolioEntries(String userId) throws IOException {
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId);
        return new ArrayList<>(portfolio.values());
    }

    /**
     * Fetches a single portfolio entry for a specific cryptocurrency.
     *
     * @param userId     User's unique ID.
     * @param cryptoName The name of the cryptocurrency.
     * @return A PortfolioEntry object for the given cryptocurrency.
     * @throws IOException if the operation fails.
     */
    public static PortfolioEntry getPortfolioEntry(String userId, String cryptoName) throws IOException {
        Map<String, PortfolioEntry> portfolio = getPortfolio(userId);
        return portfolio.getOrDefault(cryptoName, new PortfolioEntry(cryptoName, 0));
    }

    /**
     * Adds a new transaction to Firebase.
     *
     * @param userId      User's unique ID.
     * @param transaction The transaction to record.
     * @throws IOException if the operation fails.
     */
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

    public static void resetPassword(String email) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("requestType", "PASSWORD_RESET");
        requestBody.addProperty("email", email);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=" + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error resetting password: " + response.body().string());
            }
        }
    }

    public static void updateUserProfile(String userId, String newUsername, String newEmail) throws IOException {
        JsonObject userDetails = new JsonObject();
        userDetails.addProperty("username", newUsername);
        userDetails.addProperty("email", newEmail);

        RequestBody body = RequestBody.create(
                userDetails.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(DATABASE_URL + "users/" + userId + ".json")
                .patch(body) // Firebase supports PATCH for partial updates
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error updating user profile: " + response.body().string());
            }
        }
    }
}
