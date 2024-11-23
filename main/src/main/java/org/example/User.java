package org.example;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private int id; // Database ID for user
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Buy crypto
    public void buyCrypto(String cryptoName, double amount, double price, Date date) {
        addTransactionToDB(cryptoName, amount, price, date, "BUY");
    }

    // Sell crypto
    public void sellCrypto(String cryptoName, double amount, double price, Date date) {
        addTransactionToDB(cryptoName, -amount, price, date, "SELL");
    }

    private void addTransactionToDB(String cryptoName, double amount, double price, Date date, String type) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Transactions (user_id, crypto_name, amount, price, date, type) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, this.id);
                stmt.setString(2, cryptoName);
                stmt.setDouble(3, amount);
                stmt.setDouble(4, price);
                stmt.setDate(5, new java.sql.Date(date.getTime()));
                stmt.setString(6, type);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Calculate current portfolio value
    public double calculatePortfolioValue() {
        Map<String, Double> cryptoHoldings = calculateCurrentHoldings();
        double portfolioValue = 0.0;

        for (Map.Entry<String, Double> entry : cryptoHoldings.entrySet()) {
            String cryptoName = entry.getKey();
            double amountHeld = entry.getValue();
            double realTimePrice = fetchRealTimeCryptoPrice(cryptoName);

            portfolioValue += amountHeld * realTimePrice;
        }

        return portfolioValue;
    }

    // Calculate current holdings
    public Map<String, Double> calculateCurrentHoldings() {
        Map<String, Double> holdings = new HashMap<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT crypto_name, SUM(amount) AS total FROM Transactions WHERE user_id = ? GROUP BY crypto_name";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, this.id);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    holdings.put(rs.getString("crypto_name"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return holdings;
    }

    // Calculate money spent on crypto
    public double calculateMoneySpent() {
        double moneySpent = 0.0;
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT SUM(price * ABS(amount)) AS total_spent FROM Transactions WHERE user_id = ? AND type = 'BUY'";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, this.id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    moneySpent = rs.getDouble("total_spent");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moneySpent;
    }

    // Fetch real-time crypto prices (Simulated)
    private double fetchRealTimeCryptoPrice(String cryptoName) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT price FROM CryptoPrices WHERE crypto_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, cryptoName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getDouble("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Default to 0 if price not found
    }
}
