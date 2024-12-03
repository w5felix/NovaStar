package entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class TransactionTest {

    @Test
    public void testValidTransactionCreation() {
        Date transactionDate = new Date();
        Transaction transaction = new Transaction("BTC", 100, 50000, transactionDate, "BUY");
        assertNotNull(transaction, "Transaction object should not be null");
        assertEquals("BTC", transaction.getCryptoName());
        assertEquals(100, transaction.getAmount());
        assertEquals(50000, transaction.getPrice());
        assertEquals(transactionDate, transaction.getDate());
        assertEquals("BUY", transaction.getType());
    }

    @Test
    public void testTransactionCreationWithNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("BTC", -100, 50000, new Date(), "BUY");
        });
        assertEquals("Amount must be positive.", exception.getMessage());
    }

    @Test
    public void testTransactionCreationWithNegativePrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("BTC", 100, -50000, new Date(), "BUY");
        });
        assertEquals("Price must be positive.", exception.getMessage());
    }

    @Test
    public void testTransactionCreationWithInvalidType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("BTC", 100, 50000, new Date(), "HOLD");
        });
        assertEquals("Transaction type must be BUY or SELL.", exception.getMessage());
    }

    @Test
    public void testDatabaseConstructor() {
        Date transactionDate = new Date();
        Transaction transaction = new Transaction(1, "BTC", 100, 50000, transactionDate, "SELL");
        assertEquals(1, transaction.getId());
        assertEquals("SELL", transaction.getType());
    }

    @Test
    public void testSetId() {
        Date transactionDate = new Date();
        Transaction transaction = new Transaction("BTC", 100, 50000, transactionDate, "BUY");
        transaction.setId(10);
        assertEquals(10, transaction.getId());
    }

    @Test
    public void testToString() {
        Date transactionDate = new Date();
        Transaction transaction = new Transaction("BTC", 100, 50000, transactionDate, "BUY");
        String expectedString = "Transaction{id=0, cryptoName='BTC', amount=100.0, price=50000.0, date=" + transactionDate + ", type='BUY'}";
        assertEquals(expectedString, transaction.toString());
    }
}

