package testings;

import entities.PortfolioEntry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PortfolioEntryTest {

    @Test
    public void testConstructor() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 1.5);
        assertEquals("BTC", entry.getCryptoName());
        assertEquals(1.5, entry.getAmount());
    }

    @Test
    public void testAddAmount() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 1.5);
        entry.addAmount(0.5);
        assertEquals(2.0, entry.getAmount());
    }

    @Test
    public void testSubtractAmount() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 1.5);
        entry.subtractAmount(0.5);
        assertEquals(1.0, entry.getAmount());
    }

    @Test
    public void testInvalidConstructorAmount() {
        assertThrows(IllegalArgumentException.class, () -> new PortfolioEntry("BTC", -1.0));
    }

    @Test
    public void testInvalidAddAmount() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 1.5);
        assertThrows(IllegalArgumentException.class, () -> entry.addAmount(-0.5));
    }

    @Test
    public void testInvalidSubtractAmount() {
        PortfolioEntry entry = new PortfolioEntry("BTC", 1.5);
        assertThrows(IllegalArgumentException.class, () -> entry.subtractAmount(2.0));
    }
}
