package application.interactors.get_initial_investment;

import domain.entities.PortfolioEntry;
import domain.entities.Transaction;
import domain.entities.User;

public class GetInitialInvestmentInteractor {
    /**
     * Calculates the adjusted book cost (initial investment minus cost of sold units)
     * for a specific cryptocurrency in the user's portfolio.
     *
     * @param user  The user whose transactions are analyzed.
     * @param entry The portfolio entry representing the cryptocurrency.
     * @return The adjusted book cost for the cryptocurrency.
     */
    public double execute(User user, PortfolioEntry entry) {
        double totalUnits = 0.0;
        double totalBookCost = 0.0;

        for (Transaction transaction : user.getTransactions()) {
            if (transaction.getCryptoName().equalsIgnoreCase(entry.getCryptoName())) {
                if (transaction.getType().equalsIgnoreCase("BUY")) {
                    // Add the units and their cost to the total
                    totalUnits += transaction.getAmount();
                    totalBookCost += transaction.getAmount() * transaction.getPrice();
                } else if (transaction.getType().equalsIgnoreCase("SELL")) {
                    // Remove the cost of sold units from the book cost
                    double unitsSold = transaction.getAmount();
                    if (unitsSold > totalUnits) {
                        throw new IllegalArgumentException("Selling more units than currently held.");
                    }

                    double averageCostPerUnit = totalBookCost / totalUnits;
                    totalBookCost -= unitsSold * averageCostPerUnit;
                    totalUnits -= unitsSold;
                }
            }
        }

        return totalBookCost;
    }
}
