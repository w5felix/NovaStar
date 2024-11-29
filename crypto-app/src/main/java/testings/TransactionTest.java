package testings;

import entities.Transaction;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testConstructor() {
        Date date = new Date();
        Transaction transaction = new Transaction("BTC", 1.0, 50000.0, date, "BUY");
        assertEquals("BTC", transaction.getCryptoName());
        assertEquals(1.0, transaction.getAmount());
        assertEquals(50000.0, transaction.getPrice());
        assertEquals("BUY", transaction.getType());
        assertEquals(date, transaction.getDate());
    }

    @Test
    public void testInvalidAmount() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> new Transaction("BTC",
                -1.0,
                50000.0,
                date,
                "BUY"));
    }

    @Test
    public void testInvalidPrice() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> new Transaction("BTC",
                1.0,
                -50000.0,
                date,
                "BUY"));
    }

    @Test
    public void testInvalidType() {
        Date date = new Date();
        assertThrows(IllegalArgumentException.class, () -> new Transaction("BTC",
                1.0,
                50000.0,
                date,
                "INVALID"));
    }

    @Test
    public void testToString() {
        Date date = new Date();
        Transaction transaction = new Transaction("BTC",
                1.0,
                50000.0,
                date,
                "BUY");
        assertTrue(transaction.toString().contains("BTC"));
    }
}
