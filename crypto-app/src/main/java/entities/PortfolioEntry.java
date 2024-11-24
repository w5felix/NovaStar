package entities;

public class PortfolioEntry {
    private String cryptoName;
    private double amount;

    public PortfolioEntry(String cryptoName, double amount) {
        this.cryptoName = cryptoName;
        this.amount = amount;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public double getAmount() {
        return amount;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public void subtractAmount(double amount) {
        this.amount -= amount;
    }

    @Override
    public String toString() {
        return "PortfolioEntry{" +
                "cryptoName='" + cryptoName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
