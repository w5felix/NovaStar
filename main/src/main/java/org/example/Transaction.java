package org.example;

import java.time.LocalDateTime;

public class Transaction {
    private String cryptoType;
    private double amount;
    private String convertedCurrency;
    private double conversionRate;
    private LocalDateTime timestamp;

    // Constructor
    public Transaction(String cryptoType, double amount, String convertedCurrency, double conversionRate) {
        this.cryptoType = cryptoType;
        this.amount = amount;
        this.convertedCurrency = convertedCurrency;
        this.conversionRate = conversionRate;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCryptoType() {
        return cryptoType;
    }

    public double getAmount() {
        return amount;
    }

    public String getConvertedCurrency() {
        return convertedCurrency;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "cryptoType='" + cryptoType + '\'' +
                ", amount=" + amount +
                ", convertedCurrency='" + convertedCurrency + '\'' +
                ", conversionRate=" + conversionRate +
                ", timestamp=" + timestamp +
                '}';
    }
}
