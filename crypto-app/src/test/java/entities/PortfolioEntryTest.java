package entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PortfolioEntryTest {

    @Test
    public void testConstructor() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 10.0);
        assertEquals("BTC", entry.getCryptoName(), "Crypto name should match constructor input.");
        assertEquals(10.0, entry.getAmount(), "Amount should match constructor input.");
    }

    @Test
    public void testConstructorWithNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PortfolioEntry("BTC", -1.0);
        });
        assertEquals("Amount must be non-negative.", exception.getMessage(), "Exception message should indicate non-negative requirement.");
    }

    @Test
    public void testAddAmount() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 10.0);
        entry.addAmount(5.0);
        assertEquals(15.0, entry.getAmount(), "Adding amount should correctly update the total.");
    }

    @Test
    public void testAddAmountWithNegative() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 10.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            entry.addAmount(-5.0);
        });
        assertEquals("Amount to add must be positive.", exception.getMessage(), "Exception message should indicate positive amount requirement.");
    }

    @Test
    public void testSubtractAmount() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 10.0);
        entry.subtractAmount(3.0);
        assertEquals(7.0, entry.getAmount(), "Subtracting amount should correctly update the total.");
    }

    @Test
    public void testSubtractAmountExceeds() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 10.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            entry.subtractAmount(11.0);
        });
        assertEquals("Amount to subtract exceeds current holdings.", exception.getMessage(), "Exception message should warn of exceeding the amount.");
    }
}
