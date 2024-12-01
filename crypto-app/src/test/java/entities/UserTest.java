package entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class UserTest {

    @Test
    public void testUserConstructor() {
        List<Transaction> transactions = Arrays.asList(new Transaction("BTC", 100, 50000, new java.util.Date(), "BUY"));
        List<PortfolioEntry> portfolio = Arrays.asList(new PortfolioEntry("BTC", 10));

        User user = new User("1", "JohnDoe", 1000.0, transactions, portfolio);
        assertEquals("1", user.getUserId());
        assertEquals("JohnDoe", user.getUsername());
        assertEquals(1000.0, user.getCashBalance());
        assertSame(transactions, user.getTransactions());
        assertSame(portfolio, user.getPortfolio());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User("1", "JohnDoe", 1000.0, null, null);
        user.setUsername("JaneDoe");
        user.setCashBalance(2000.0);

        assertEquals("JaneDoe", user.getUsername());
        assertEquals(2000.0, user.getCashBalance());

        // Test setting and getting transactions
        List<Transaction> newTransactions = Arrays.asList(new Transaction("ETH", 50, 3000, new java.util.Date(), "SELL"));
        user.setTransactions(newTransactions);
        assertSame(newTransactions, user.getTransactions());

        // Test setting and getting portfolio entries
        List<PortfolioEntry> newPortfolio = Arrays.asList(new PortfolioEntry("ETH", 5));
        user.setPortfolio(newPortfolio);
        assertSame(newPortfolio, user.getPortfolio());
    }

    @Test
    public void testToString() {
        User user = new User("1", "JohnDoe", 1000.0, null, null);
        String expected = "User{userId='1', username='JohnDoe', cashBalance=1000.0, transactions=null, portfolio=null}";
        assertEquals(expected, user.toString());
    }
}

