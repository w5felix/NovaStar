package views;

import data_access.BlockChainAPIClient.CryptoInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CryptoPricesView extends JPanel {

    private final JPanel cryptoPricesPanel;

    public CryptoPricesView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Crypto Prices");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        cryptoPricesPanel = new JPanel();
        cryptoPricesPanel.setLayout(new BoxLayout(cryptoPricesPanel, BoxLayout.Y_AXIS));

        add(headerLabel, BorderLayout.NORTH);
        add(new JScrollPane(cryptoPricesPanel), BorderLayout.CENTER);
    }

    public void updateCryptoPrices(List<CryptoInfo> cryptoPrices, CryptoActionHandler actionHandler) {
        cryptoPricesPanel.removeAll();

        if (cryptoPrices.isEmpty()) {
            cryptoPricesPanel.add(new JLabel("No cryptocurrency prices available."));
        } else {
            for (CryptoInfo crypto : cryptoPrices) {
                JPanel cryptoCard = new JPanel(new BorderLayout());
                cryptoCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                cryptoCard.setBackground(Color.WHITE);

                JLabel nameLabel = new JLabel(crypto.getName() + " (" + crypto.getSymbol() + ")");
                nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
                nameLabel.setForeground(new Color(33, 150, 243));

                JLabel priceLabel = new JLabel(String.format("$%.2f", crypto.getCurrentPrice()));
                priceLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

                JLabel changeLabel = new JLabel(String.format("%.2f%%", crypto.getPercentageChange()));
                changeLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                changeLabel.setForeground(crypto.getPercentageChange() > 0 ?
                        new Color(76, 175, 80) : new Color(244, 67, 54));

                cryptoCard.add(nameLabel, BorderLayout.WEST);
                cryptoCard.add(priceLabel, BorderLayout.CENTER);
                cryptoCard.add(changeLabel, BorderLayout.EAST);

                cryptoCard.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        actionHandler.onCryptoClicked(crypto);
                    }
                });

                cryptoPricesPanel.add(cryptoCard);
                cryptoPricesPanel.add(Box.createVerticalStrut(10));
            }
        }

        cryptoPricesPanel.revalidate();
        cryptoPricesPanel.repaint();
    }

    public interface CryptoActionHandler {
        void onCryptoClicked(CryptoInfo crypto);
    }
}
