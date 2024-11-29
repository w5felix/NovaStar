package entities;

import java.util.Date;

/**
 * Represents a price alert set by a user for a specific cryptocurrency.
 */
public class Alert {

    private String alertId;       // Unique ID for the alert (Firebase generated or UUID)
    private String userId;        // User ID associated with the alert
    private String cryptoName;    // Name of the cryptocurrency (e.g., "BTC")
    private double targetPrice;   // Target price for the alert
    private boolean isAbove;      // True if alert triggers when price exceeds target
    private Date creationDate;    // Date when the alert was created
    private boolean triggered;    // Whether the alert has been triggered

    /**
     * Constructor for creating a new alert.
     *
     * @param userId      User ID who owns the alert.
     * @param cryptoName  Name of the cryptocurrency.
     * @param targetPrice Target price for the alert.
     * @param isAbove     True if alert triggers when price exceeds target price.
     */
    public Alert(String userId, String cryptoName, double targetPrice, boolean isAbove) {
        this.userId = userId;
        this.cryptoName = cryptoName;
        this.targetPrice = targetPrice;
        this.isAbove = isAbove;
        this.creationDate = new Date();
        this.triggered = false;
    }

    // Getters and Setters

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public boolean isAbove() {
        return isAbove;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    /**
     * Checks if the alert condition is met based on the current price.
     *
     * @param currentPrice The current price of the cryptocurrency.
     * @return True if the condition is met, false otherwise.
     */
    public boolean checkAlert(double currentPrice) {
        return isAbove ? currentPrice > targetPrice : currentPrice < targetPrice;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertId='" + alertId + '\'' +
                ", userId='" + userId + '\'' +
                ", cryptoName='" + cryptoName + '\'' +
                ", targetPrice=" + targetPrice +
                ", isAbove=" + isAbove +
                ", creationDate=" + creationDate +
                ", triggered=" + triggered +
                '}';
    }
}
