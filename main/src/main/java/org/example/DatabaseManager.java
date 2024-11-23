package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:crypto_portfolio.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection()) {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY, name TEXT)";
            String createTransactionsTable = """
                    CREATE TABLE IF NOT EXISTS Transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id INTEGER,
                        crypto_name TEXT,
                        amount REAL,
                        price REAL,
                        date DATE,
                        type TEXT,
                        FOREIGN KEY(user_id) REFERENCES Users(id)
                    )
                    """;
            String createCryptoPricesTable = "CREATE TABLE IF NOT EXISTS CryptoPrices (crypto_name TEXT PRIMARY KEY, price REAL)";

            connection.createStatement().execute(createUsersTable);
            connection.createStatement().execute(createTransactionsTable);
            connection.createStatement().execute(createCryptoPricesTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

