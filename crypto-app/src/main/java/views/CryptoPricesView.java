package views;

import infrastructure.api_clients.BlockChainApiClient.CryptoInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class CryptoPricesView extends JPanel {

    private final JPanel cryptoPricesPanel;
    private final JComboBox<String> sortCriteriaComboBox;
    private List<CryptoInfo> currentCryptoPrices; // Store the current crypto prices for re-sorting
    private CryptoActionHandler currentActionHandler; // Store the action handler for re-sorting

    public CryptoPricesView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Crypto Prices");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Sorting options
        JPanel topPanel = new JPanel(new BorderLayout());
        sortCriteriaComboBox = new JComboBox<>(new String[]{"Price: High to Low", "Price: Low to High", "Growth: High to Low", "Growth: Low to High"});
        sortCriteriaComboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        sortCriteriaComboBox.addActionListener(e -> updateSorting());

        topPanel.add(headerLabel, BorderLayout.NORTH);
        topPanel.add(sortCriteriaComboBox, BorderLayout.SOUTH);

        cryptoPricesPanel = new JPanel();
        cryptoPricesPanel.setLayout(new BoxLayout(cryptoPricesPanel, BoxLayout.Y_AXIS));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(cryptoPricesPanel), BorderLayout.CENTER);
    }

    public void updateCryptoPrices(List<CryptoInfo> cryptoPrices, CryptoActionHandler actionHandler) {
        this.currentCryptoPrices = new ArrayList<>(cryptoPrices); // Store a copy of the list
        this.currentActionHandler = actionHandler; // Store the action handler
        sortAndDisplayCryptoPrices();
    }

    private void sortAndDisplayCryptoPrices() {
        if (currentCryptoPrices == null || currentActionHandler == null) {
            return; // If no prices or action handler is set, do nothing
        }

        // Filter out invalid data
        currentCryptoPrices.removeIf(crypto ->
                crypto.getCurrentPrice() < 0.000001 ||
                        Double.isInfinite(crypto.getPercentageChange()) ||
                        Double.isNaN(crypto.getPercentageChange())
        );

        // Perform sorting based on the selected criteria
        String selectedSort = (String) sortCriteriaComboBox.getSelectedItem();
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Price: High to Low":
                    currentCryptoPrices.sort(Comparator.comparingDouble(CryptoInfo::getCurrentPrice).reversed());
                    break;
                case "Price: Low to High":
                    currentCryptoPrices.sort(Comparator.comparingDouble(CryptoInfo::getCurrentPrice));
                    break;
                case "Growth: High to Low":
                    currentCryptoPrices.sort(Comparator.comparingDouble(CryptoInfo::getPercentageChange).reversed());
                    break;
                case "Growth: Low to High":
                    currentCryptoPrices.sort(Comparator.comparingDouble(CryptoInfo::getPercentageChange));
                    break;
            }
        }

        cryptoPricesPanel.removeAll();

        if (currentCryptoPrices.isEmpty()) {
            cryptoPricesPanel.add(new JLabel("No valid cryptocurrency prices available."));
        } else {
            for (CryptoInfo crypto : currentCryptoPrices) {
                JPanel cryptoCard = new JPanel(new BorderLayout());
                cryptoCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                cryptoCard.setBackground(Color.WHITE);

                JLabel nameLabel = new JLabel(crypto.getName() + " (" + crypto.getSymbol() + ")");
                nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
                nameLabel.setForeground(new Color(33, 150, 243));

                JLabel priceLabel = new JLabel(String.format("$%.6f", crypto.getCurrentPrice())); // More decimals for price
                priceLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

                JLabel changeLabel = new JLabel(String.format("%.6f%%", crypto.getPercentageChange())); // More decimals for percentage change
                changeLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                changeLabel.setForeground(crypto.getPercentageChange() > 0 ?
                        new Color(76, 175, 80) : new Color(244, 67, 54));

                cryptoCard.add(nameLabel, BorderLayout.WEST);
                cryptoCard.add(priceLabel, BorderLayout.CENTER);
                cryptoCard.add(changeLabel, BorderLayout.EAST);

                cryptoCard.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        currentActionHandler.onCryptoClicked(crypto);
                    }
                });

                cryptoPricesPanel.add(cryptoCard);
                cryptoPricesPanel.add(Box.createVerticalStrut(10));
            }
        }

        cryptoPricesPanel.revalidate();
        cryptoPricesPanel.repaint();
    }

    private void updateSorting() {
        sortAndDisplayCryptoPrices(); // Trigger re-sorting and re-display
    }

    public interface CryptoActionHandler {
        void onCryptoClicked(CryptoInfo crypto);
    }
}
