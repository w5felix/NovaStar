package testings;

import entities.User;
import entities.PortfolioEntry;
import entities.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserLoginConstructor() throws IOException {
        // Assuming valid credentials
        User user = new User("testuser@example.com", "password123");

        assertNotNull(user.getUserId(), "User ID should not be null after successful login");
        assertEquals("testuser@example.com", user.getName(), "User name should default to email");
        assertNotNull(user.getPortfolio(), "Portfolio should not be null after initialization");
        assertNotNull(user.getTransactions(), "Transaction history should not be null after initialization");
    }

    @Test
    public void testUserRegistrationConstructor() throws IOException {
        // Assuming successful registration
        User user = new User("testuser", "testuser@example.com", "password123", "Favorite color?", "Blue");

        assertNotNull(user.getUserId(), "User ID should not be null after successful registration");
        assertEquals("testuser", user.getName(), "User name should match the username provided");
        assertEquals(0.0, user.getCashBalance(), "Initial cash balance should be 0.0");
        assertTrue(user.getPortfolio().isEmpty(), "Portfolio should be empty after registration");
        assertTrue(user.getTransactions().isEmpty(), "Transaction history should be empty after registration");
    }

    @Test
    public void testDepositCash() throws IOException {
        User user = new User("testuser@example.com", "password123");

        user.depositCash(500.0);
        assertEquals(1500.0, user.getCashBalance(), "Cash balance should reflect the deposited amount");

        assertThrows(IllegalArgumentException.class, () -> user.depositCash(-100.0), "Should throw exception for negative deposit");
    }

    @Test
    public void testWithdrawCash() throws IOException {
        User user = new User("testuser@example.com", "password123");

        user.withdrawCash(500.0);
        assertEquals(500.0, user.getCashBalance(), "Cash balance should reflect the withdrawn amount");

        assertThrows(IllegalArgumentException.class, () -> user.withdrawCash(-100.0), "Should throw exception for negative withdrawal");
        assertThrows(IllegalArgumentException.class, () -> user.withdrawCash(2000.0), "Should throw exception for insufficient funds");
    }

    @Test
    public void testBuyCrypto() throws Exception {
        User user = new User("testuser@example.com", "password123");

        user.buyCrypto("BTC", 0.01);
        List<PortfolioEntry> portfolio = user.getPortfolio();

        assertEquals(1, portfolio.size(), "Portfolio should have one entry after buying crypto");
        assertEquals(0.01, portfolio.get(0).getAmount(), "Portfolio entry amount should match the amount bought");
        assertTrue(user.getCashBalance() < 1000.0, "Cash balance should decrease after buying crypto");
    }

    @Test
    public void testSellCrypto() throws Exception {
        User user = new User("testuser@example.com", "password123");

        // Buy first to have crypto to sell
        user.buyCrypto("BTC", 0.01);

        user.sellCrypto("BTC", 0.005);
        List<PortfolioEntry> portfolio = user.getPortfolio();

        assertEquals(0.005, portfolio.get(0).getAmount(), "Portfolio entry amount should decrease after selling crypto");
        assertTrue(user.getCashBalance() > 1000.0, "Cash balance should increase after selling crypto");
    }

    @Test
    public void testSellCryptoInsufficientHoldings() throws Exception {
        User user = new User("testuser@example.com", "password123");

        // Buy first to have crypto to sell
        user.buyCrypto("BTC", 0.01);

        assertThrows(IllegalArgumentException.class, () -> user.sellCrypto("BTC", 0.02), "Should throw exception for insufficient holdings");
    }

    @Test
    public void testCalculatePortfolioValue() throws Exception {
        User user = new User("testuser@example.com", "password123");

        user.buyCrypto("BTC", 0.01);
        user.buyCrypto("ETH", 1.0);

        double portfolioValue = user.calculatePortfolioValue();

        assertTrue(portfolioValue > user.getCashBalance(), "Portfolio value should include crypto holdings and cash balance");
    }

    @Test
    public void testAddAlert() throws IOException {
        User user = new User("testuser@example.com", "password123");

        user.addAlert("BTC", 50000.0, true);
        List<entities.Alert> alerts = user.getAlerts();

        assertEquals(1, alerts.size(), "There should be one alert after adding");
        assertEquals("BTC", alerts.get(0).getCryptoName(), "Alert cryptoName should match the one added");
        assertEquals(50000.0, alerts.get(0).getTargetPrice(), "Alert targetPrice should match the one added");
    }

    @Test
    public void testAddAlertAndTrigger() throws Exception {
        User user = new User("testuser@example.com", "password123");

        user.addAlert("BTC", 50000.0, true);

        List<entities.Alert> alerts = user.getAlerts();
        entities.Alert alert = alerts.get(0);

        // Simulate a price exceeding the target
        alert.setTriggered(true);
        assertTrue(alert.isTriggered(), "Alert should be triggered");
    }

    @Test
    public void testPortfolioAndTransactionsSync() throws Exception {
        User user = new User("testuser@example.com", "password123");

        user.buyCrypto("BTC", 0.01);
        user.sellCrypto("BTC", 0.005);

        List<PortfolioEntry> portfolio = user.getPortfolio();
        List<Transaction> transactions = user.getTransactions();

        assertEquals(1, portfolio.size(), "Portfolio should have one entry");
        assertEquals(0.005, portfolio.get(0).getAmount(), "Portfolio amount should match remaining holdings");
        assertEquals(2, transactions.size(), "Transaction history should have two entries");
    }
}
