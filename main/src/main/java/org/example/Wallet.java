package org.example;

import java.util.HashMap;
import java.util.Map;

public class Wallet {
    private Map<String, Double> cryptoHoldings;
    private double fiatBalance;

    public Wallet() {
        this.cryptoHoldings = new HashMap<>();
        this.fiatBalance = 0.0;
    }

    public Map<String, Double> getCryptoHoldings() {
        return cryptoHoldings;
    }

    public void addCrypto(String cryptoName, double amount) {
        this.cryptoHoldings.put(cryptoName, this.cryptoHoldings.getOrDefault(cryptoName, 0.0) + amount);
    }

    public double getFiatBalance() {
        return fiatBalance;
    }

    public void setFiatBalance(double fiatBalance) {
        this.fiatBalance = fiatBalance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "cryptoHoldings=" + cryptoHoldings +
                ", fiatBalance=" + fiatBalance +
                '}';
    }
}
