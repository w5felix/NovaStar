package testings;

import entities.Alert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertTest {

    @Test
    public void testConstructor() {
        Alert alert = new Alert("user1", "BTC", 50000.0, true);
        assertEquals("user1", alert.getUserId());
        assertEquals("BTC", alert.getCryptoName());
        assertEquals(50000.0, alert.getTargetPrice());
        assertTrue(alert.isAbove());
        assertFalse(alert.isTriggered());
    }

    @Test
    public void testSetTriggered() {
        Alert alert = new Alert("user1", "BTC", 50000.0, true);
        alert.setTriggered(true);
        assertTrue(alert.isTriggered());
    }

    @Test
    public void testCheckAlertAboveTarget() {
        Alert alert = new Alert("user1", "BTC", 50000.0, true);
        assertTrue(alert.checkAlert(51000.0));
        assertFalse(alert.checkAlert(49000.0));
    }

    @Test
    public void testCheckAlertBelowTarget() {
        Alert alert = new Alert("user1", "BTC", 50000.0, false);
        assertTrue(alert.checkAlert(49000.0));
        assertFalse(alert.checkAlert(51000.0));
    }

    @Test
    public void testToString() {
        Alert alert = new Alert("user1", "BTC", 50000.0, true);
        assertTrue(alert.toString().contains("user1"));
        assertTrue(alert.toString().contains("BTC"));
    }
}
