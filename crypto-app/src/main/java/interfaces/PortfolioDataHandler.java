package interfaces;

import entities.PortfolioEntry;

/**
 * A handler interface for calculating portfolio data such as current value and initial investment.
 * This allows the view to remain independent of the actual logic.
 */
public interface PortfolioDataHandler {
    /**
     * Calculates the current value of a portfolio entry.
     *
     * @param entry The portfolio entry.
     * @return The current value of the portfolio entry.
     */
    double getCurrentValue(PortfolioEntry entry);

    /**
     * Calculates the initial investment for a portfolio entry.
     *
     * @param entry The portfolio entry.
     * @return The initial investment for the portfolio entry.
     */
    double getInitialInvestment(PortfolioEntry entry);
}
