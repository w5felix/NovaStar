package testings;

import api.FireBaseAPIClient;
import entities.Alert;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlertServiceTest {

    @BeforeEach
    public void setup() throws Exception {
        // Clean up Firebase alerts before each test
        List<Alert> existingAlerts = FireBaseAPIClient.getUserAlerts("test_user");
        for (Alert alert : existingAlerts) {
            FireBaseAPIClient.deleteAlert(alert.getUserId(), alert.getAlertId());
        }
    }

    @Test
    public void testMonitorAlerts() throws Exception {
        // Save an alert to Firebase
        Alert alert = new Alert("test_user", "BTC", 50000.0, true);
        FireBaseAPIClient.saveAlert("test_user", alert);

        // Trigger the alert by simulating a price above the target
        double currentPrice = 51000.0;
        alert.setTriggered(alert.checkAlert(currentPrice));

        // Verify the alert is triggered
        FireBaseAPIClient.updateAlert(alert.getUserId(), alert);
        List<Alert> alerts = FireBaseAPIClient.getUserAlerts("test_user");

        assertTrue(alerts.get(0).isTriggered(), "The alert should be triggered when the current price is above the target.");
    }

    @Test
    public void testMultipleUserAlerts() throws Exception {
        // Create alerts for multiple users
        Alert user1Alert = new Alert("user1", "BTC", 50000.0, true);
        Alert user2Alert = new Alert("user2", "ETH", 2000.0, false);

        FireBaseAPIClient.saveAlert("user1", user1Alert);
        FireBaseAPIClient.saveAlert("user2", user2Alert);

        // Simulate price updates
        double btcPrice = 51000.0; // Should trigger user1's alert
        double ethPrice = 2100.0;  // Should not trigger user2's alert

        user1Alert.setTriggered(user1Alert.checkAlert(btcPrice));
        user2Alert.setTriggered(user2Alert.checkAlert(ethPrice));

        // Update alerts in Firebase
        FireBaseAPIClient.updateAlert("user1", user1Alert);
        FireBaseAPIClient.updateAlert("user2", user2Alert);

        // Verify user1's alert is triggered, and user2's alert is not
        List<Alert> user1Alerts = FireBaseAPIClient.getUserAlerts("user1");
        List<Alert> user2Alerts = FireBaseAPIClient.getUserAlerts("user2");

        assertTrue(user1Alerts.get(0).isTriggered(), "User1's alert should be triggered.");
        assertFalse(user2Alerts.get(0).isTriggered(), "User2's alert should not be triggered.");
    }

    @Test
    public void testAlertNotTriggered() throws Exception {
        // Save an alert that should not trigger
        Alert alert = new Alert("test_user", "BTC", 50000.0, true);
        FireBaseAPIClient.saveAlert("test_user", alert);

        // Simulate a price below the target
        double currentPrice = 49000.0;
        alert.setTriggered(alert.checkAlert(currentPrice));

        // Update alert in Firebase
        FireBaseAPIClient.updateAlert("test_user", alert);

        // Verify the alert is not triggered
        List<Alert> alerts = FireBaseAPIClient.getUserAlerts("test_user");
        assertFalse(alerts.get(0).isTriggered(), "The alert should not be triggered when the current price is below the target.");
    }
}

