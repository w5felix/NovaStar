package testings;

import entities.User;
import entities.PortfolioEntry;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PortfolioManagementTest {

    private User user;

    @BeforeEach
    public void setup() throws Exception {
        // Initialize a new user with a clean Firebase state
        user = new User("testuser@example.com", "password123");
        user.depositCash(10000.0); // Deposit initial funds
    }

    @Test
    public void testEmptyPortfolio() throws Exception {
        List<PortfolioEntry> portfolio = user.getPortfolio();
        assertTrue(portfolio.isEmpty(), "Portfolio should be empty for a new user.");
    }

    @Test
    public void testBuyCrypto() throws Exception {
        user.buyCrypto("BTC", 0.01); // Buy 0.01 BTC
        List<PortfolioEntry> portfolio = user.getPortfolio();

        assertEquals(1, portfolio.size(), "Portfolio should contain one entry.");
        assertEquals(0.01, portfolio.get(0).getAmount(), "BTC amount should match the purchased quantity.");
    }

    @Test
    public void testSellCrypto() throws Exception {
        // Buy and sell cryptocurrency
        user.buyCrypto("BTC", 0.01);
        user.sellCrypto("BTC", 0.005); // Sell half

        List<PortfolioEntry> portfolio = user.getPortfolio();
        assertEquals(0.005, portfolio.get(0).getAmount(), "BTC amount should be updated after selling.");
    }

    @Test
    public void testInsufficientFundsForBuying() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.buyCrypto("BTC", 1.0); // Try to buy with insufficient funds
        });
        assertEquals("Insufficient cash balance to buy BTC", exception.getMessage());
    }

    @Test
    public void testInsufficientHoldingsForSelling() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.sellCrypto("BTC", 0.01); // Try to sell without holdings
        });
        assertEquals("Insufficient holdings to sell BTC", exception.getMessage());
    }
}

