package services;

import api.BlockChainAPIClient;
import api.FireBaseAPIClient;
import entities.Alert;

import java.util.List;

/**
 * Service to monitor alerts and trigger notifications.
 */
public class AlertService {

    /**
     * Monitors and processes all active alerts for all users.
     *
     * @throws Exception If there is an issue fetching prices or updating Firebase.
     */
    public static void monitorAlerts() throws Exception {
        // Fetch all active alerts from Firebase
        List<Alert> alerts = FireBaseAPIClient.getUserAlerts("all_users"); // Fetch alerts for all users

        for (Alert alert : alerts) {
            double currentPrice = BlockChainAPIClient.getCurrentPrice(alert.getCryptoName() + "-USD");

            if (alert.checkAlert(currentPrice)) {
                alert.setTriggered(true);

                // Update the alert in Firebase
                FireBaseAPIClient.updateAlert(alert.getUserId(), alert);
                sendNotification(alert); // Notify the user
            }
        }
    }

    /**
     * Sends a notification to the user when an alert is triggered.
     *
     * @param alert The triggered alert.
     */
    public static void sendNotification(Alert alert) {
        System.out.println("ALERT TRIGGERED: " + alert.getCryptoName() +
                " has reached your target price of " + alert.getTargetPrice());
        // Implement notification system (e.g., Firebase Cloud Messaging or email)
    }
}
