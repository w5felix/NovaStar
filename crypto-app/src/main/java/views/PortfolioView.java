package views;

import entities.PortfolioEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PortfolioView extends JPanel {

    private final JPanel portfolioPanel;

    public PortfolioView() {
        setLayout(new BorderLayout());
        portfolioPanel = new JPanel();
        portfolioPanel.setLayout(new BoxLayout(portfolioPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(portfolioPanel), BorderLayout.CENTER);
    }

    /**
     * Updates the portfolio display with the given portfolio entries and a data handler
     * for fetching calculations (e.g., current value and growth).
     *
     * @param portfolio   The list of portfolio entries to display.
     * @param dataHandler The handler for calculating current values and growth.
     */
    public void updatePortfolio(List<PortfolioEntry> portfolio, PortfolioDataHandler dataHandler) {
        portfolioPanel.removeAll();

        if (portfolio.isEmpty()) {
            portfolioPanel.add(new JLabel("No cryptocurrency holdings found."));
        } else {
            for (PortfolioEntry entry : portfolio) {
                // Create a portfolio card for each entry
                JPanel portfolioCard = new JPanel(new BorderLayout());
                portfolioCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                portfolioCard.setBackground(Color.WHITE);

                // Cryptocurrency name label
                JLabel nameLabel = new JLabel(entry.getCryptoName());
                nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

                // Value and growth label
                double currentValue = dataHandler.getCurrentValue(entry);
                double initialInvestment = dataHandler.getInitialInvestment(entry);
                double growth = currentValue - initialInvestment;
                double growthPercentage = (initialInvestment > 0) ? (growth / initialInvestment) * 100 : 0;

                JLabel valueLabel = new JLabel(String.format("Value: $%.2f (Growth: %.2f%%)", currentValue, growthPercentage));
                valueLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                valueLabel.setForeground(growth >= 0 ? new Color(76, 175, 80) : new Color(244, 67, 54));

                // Add labels to the card
                portfolioCard.add(nameLabel, BorderLayout.WEST);
                portfolioCard.add(valueLabel, BorderLayout.EAST);

                // Add the card to the panel
                portfolioPanel.add(portfolioCard);
                portfolioPanel.add(Box.createVerticalStrut(10)); // Space between cards
            }
        }

        portfolioPanel.revalidate();
        portfolioPanel.repaint();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * A handler interface for fetching portfolio-related calculations such as current value and initial investment.
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
}
