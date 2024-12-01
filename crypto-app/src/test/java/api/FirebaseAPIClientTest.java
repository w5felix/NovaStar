package api;

import api.FireBaseAPIClient;
import entities.PortfolioEntry;
import entities.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class FirebaseAPIClientTest {

    @Test
    public void testRegisterUser() {
        try {
            // Generate a unique email address using the current timestamp
            String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";

            // Call the registerUser method
            String userId = FireBaseAPIClient.registerUser(uniqueEmail, "password123");

            // Validate that the user ID is not null
            assertNotNull(userId, "User ID should not be null.");

            System.out.println("Successfully registered user with email: " + uniqueEmail);
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }


    @Test
    public void testLoginUser() {
        try {
            String userId = FireBaseAPIClient.loginUser("testuser@example.com", "password123");
            assertNotNull(userId, "User ID should not be null.");
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testAddCash() {
        try {
            // Reset cash reserves for the test user
            FireBaseAPIClient.updateCashReserves("testUserId", 0.0);

            // Add cash
            FireBaseAPIClient.addCash("testUserId", 500);

            // Fetch updated cash reserves
            double cash = FireBaseAPIClient.getCashReserves("testUserId");

            // Assert the cash reserves are updated correctly
            assertEquals(500, cash, 0.01, "Cash reserves should be updated.");
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }


    @Test
    public void testTransactionHandling() {
        try {
            // Step 1: Initialize sufficient cash reserves for the test user
            FireBaseAPIClient.updateCashReserves("testUserId", 100000.0); // Ensure the user has enough cash for the transaction

            // Step 2: Create a BUY transaction
            Transaction buyTransaction = new Transaction("BTC", 1.0, 50000, new java.util.Date(), "BUY");

            // Step 3: Add the transaction and update the portfolio
            FireBaseAPIClient.addTransactionAndUpdatePortfolio("testUserId", buyTransaction);

            // Step 4: Fetch transactions to verify the update
            List<Transaction> transactions = FireBaseAPIClient.getTransactions("testUserId");
            assertFalse(transactions.isEmpty(), "Transactions list should not be empty.");

            // Step 5: Verify the cash reserves were updated correctly
            double updatedCashReserves = FireBaseAPIClient.getCashReserves("testUserId");
            assertEquals(50000.0, updatedCashReserves, 0.01, "Cash reserves should be updated after the transaction.");
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }


    @Test
    public void testAddTransactionAndUpdatePortfolio() {
        try {
            // Step 1: Set up initial portfolio with a known amount of BTC
            FireBaseAPIClient.updatePortfolio("testUserId", new Transaction("BTC", 5.0, 1.0, new java.util.Date(), "BUY"));

            // Step 2: Reset initial cash reserves
            FireBaseAPIClient.updateCashReserves("testUserId", 50000.0);

            // Step 3: Fetch the initial portfolio entry
            PortfolioEntry initialEntry = FireBaseAPIClient.getPortfolioEntry("testUserId", "BTC");
            double initialAmount = initialEntry != null ? initialEntry.getAmount() : 0.0;

            // Step 4: Create and process a BUY transaction
            Transaction transaction = new Transaction("BTC", 1.0, 40000.0, new java.util.Date(), "BUY");
            FireBaseAPIClient.addTransactionAndUpdatePortfolio("testUserId", transaction);

            // Step 5: Validate portfolio updates
            PortfolioEntry updatedEntry = FireBaseAPIClient.getPortfolioEntry("testUserId", "BTC");
            double expectedAmount = initialAmount + 1.0; // Initial amount + transaction amount
            assertEquals(expectedAmount, updatedEntry.getAmount(), 0.01, "Portfolio should reflect the purchased BTC.");

            // Step 6: Validate cash reserves
            double expectedCashReserves = 50000.0 - (transaction.getAmount() * transaction.getPrice());
            double updatedCashReserves = FireBaseAPIClient.getCashReserves("testUserId");
            assertEquals(expectedCashReserves, updatedCashReserves, 0.01, "Cash reserves should be updated correctly.");

        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }
}
